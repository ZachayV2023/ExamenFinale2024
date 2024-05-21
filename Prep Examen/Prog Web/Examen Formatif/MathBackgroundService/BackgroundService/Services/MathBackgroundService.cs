using BackgroundServiceVote.Hubs;
using BackgroundServiceVote.Models;
using Microsoft.AspNetCore.SignalR;
using Microsoft.EntityFrameworkCore;
using BackgroundServiceVote.Data;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace BackgroundServiceVote.Services
{
    public class UserData
    {
        public int Choice { get; set; } = -1;
        public int NbConnections { get; set; } = 0;
    }

    public class MathBackgroundService : BackgroundService
    {
        public const int DELAY = 20 * 1000;

        private Dictionary<string, UserData> _data = new();
        private readonly IHubContext<MathQuestionsHub> _mathQuestionHub;
        private readonly MathQuestionsService _mathQuestionsService;
        private readonly IServiceScopeFactory _scopeFactory;
        private MathQuestion? _currentQuestion;

        public MathQuestion? CurrentQuestion => _currentQuestion;

        public MathBackgroundService(IHubContext<MathQuestionsHub> mathQuestionHub, MathQuestionsService mathQuestionsService, IServiceScopeFactory scopeFactory)
        {
            _mathQuestionHub = mathQuestionHub;
            _mathQuestionsService = mathQuestionsService;
            _scopeFactory = scopeFactory;
        }

        public void AddUser(string userId)
        {
            if (!_data.ContainsKey(userId))
            {
                _data[userId] = new UserData();
            }
            _data[userId].NbConnections++;
        }

        public void RemoveUser(string userId)
        {
            if (_data.ContainsKey(userId))
            {
                _data[userId].NbConnections--;
                if (_data[userId].NbConnections <= 0)
                    _data.Remove(userId);
            }
        }

        public async void SelectChoice(string userId, int choice)
        {
            if (_currentQuestion == null)
                return;

            UserData userData = _data[userId];

            if (userData.Choice != -1)
                throw new Exception("A user cannot change their choice!");

            userData.Choice = choice;
            _currentQuestion.PlayerChoices[choice]++;

            await _mathQuestionHub.Clients.All.SendAsync("IncreasePlayersChoices", choice);
        }

        private async Task EvaluateChoices()
        {
            using var scope = _scopeFactory.CreateScope();
            var context = scope.ServiceProvider.GetRequiredService<BackgroundServiceContext>();

            foreach (var userId in _data.Keys)
            {
                var userData = _data[userId];

                if (userData.Choice == _currentQuestion!.RightAnswerIndex)
                {
                    await _mathQuestionHub.Clients.User(userId).SendAsync("PlayerRightAnswer", true, _currentQuestion.Answers[_currentQuestion.RightAnswerIndex]);
                    var player = await context.Player.SingleOrDefaultAsync(p => p.UserId == userId);
                    if (player != null)
                    {
                        player.NbRightAnswers++;
                        context.Update(player);
                    }
                }
                else
                {
                    await _mathQuestionHub.Clients.User(userId).SendAsync("PlayerRightAnswer", false, _currentQuestion.Answers[_currentQuestion.RightAnswerIndex]);
                }
            }

            await context.SaveChangesAsync();

            foreach (var key in _data.Keys)
            {
                _data[key].Choice = -1;
            }
        }

        private async Task Update(CancellationToken stoppingToken)
        {
            if (_currentQuestion != null)
            {
                await EvaluateChoices();
            }

            _currentQuestion = _mathQuestionsService.CreateQuestion();

            await _mathQuestionHub.Clients.All.SendAsync("CurrentQuestion", _currentQuestion);
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            while (!stoppingToken.IsCancellationRequested)
            {
                await Update(stoppingToken);
                await Task.Delay(DELAY, stoppingToken);
            }
        }
    }
}
