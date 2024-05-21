using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using System.Security.Claims;
using WebAPI.Controllers;
using WebAPI.Exceptions;
using WebAPI.Models;
using WebAPI.Services;

namespace WebAPI.Tests
{
    [TestClass]
    public class SeatsControllerTests
    {
        private const string TestUserId = "123456789";

        [TestMethod]
        public void ReserveSeat_ReturnsSeat_WhenSuccessful()
        {
            var serviceMock = new Mock<SeatsService>();
            var controller = new SeatsController(serviceMock.Object);
            controller.ControllerContext = new ControllerContext()
            {
                HttpContext = new DefaultHttpContext()
                {
                    User = new ClaimsPrincipal(new ClaimsIdentity(new Claim[]
                    {
                        new Claim(ClaimTypes.NameIdentifier, TestUserId)
                    }))
                }
            };

            var seatNumber = 1;
            var expectedSeat = new Seat() { Number = seatNumber };
            serviceMock.Setup(s => s.ReserveSeat(TestUserId, seatNumber)).Returns(expectedSeat);

            var actionResult = controller.ReserveSeat(seatNumber);

            var result = actionResult.Result as OkObjectResult;

            Assert.IsNotNull(result);
            Assert.AreEqual(expectedSeat, result.Value);
            serviceMock.Verify(s => s.ReserveSeat(TestUserId, seatNumber), Times.Once);
        }

        [TestMethod]
        public void ReserveSeat_ReturnsUnauthorized_WhenSeatAlreadyTaken()
        {
            var serviceMock = new Mock<SeatsService>();
            var controller = new SeatsController(serviceMock.Object);
            controller.ControllerContext = new ControllerContext()
            {
                HttpContext = new DefaultHttpContext()
                {
                    User = new ClaimsPrincipal(new ClaimsIdentity(new Claim[]
                    {
                        new Claim(ClaimTypes.NameIdentifier, TestUserId)
                    }))
                }
            };

            var seatNumber = 2;
            serviceMock.Setup(s => s.ReserveSeat(TestUserId, seatNumber)).Throws(new SeatAlreadyTakenException());

            var actionResult = controller.ReserveSeat(seatNumber);

            var result = actionResult.Result as UnauthorizedResult;

            Assert.IsNotNull(result);
            serviceMock.Verify(s => s.ReserveSeat(TestUserId, seatNumber), Times.Once);
        }

        [TestMethod]
        public void ReserveSeat_ReturnsNotFound_WhenSeatNumberOutOfBounds()
        {
            var serviceMock = new Mock<SeatsService>();
            var controller = new SeatsController(serviceMock.Object);
            controller.ControllerContext = new ControllerContext()
            {
                HttpContext = new DefaultHttpContext()
                {
                    User = new ClaimsPrincipal(new ClaimsIdentity(new Claim[]
                    {
                        new Claim(ClaimTypes.NameIdentifier, TestUserId)
                    }))
                }
            };

            var seatNumber = 101;
            serviceMock.Setup(s => s.ReserveSeat(TestUserId, seatNumber)).Throws(new SeatOutOfBoundsException());

            var actionResult = controller.ReserveSeat(seatNumber);

            var result = actionResult.Result as NotFoundObjectResult;

            Assert.IsNotNull(result);
            Assert.AreEqual("Ne peux pas trouver " + seatNumber, result.Value);
            serviceMock.Verify(s => s.ReserveSeat(TestUserId, seatNumber), Times.Once);
        }

        [TestMethod]
        public void ReserveSeat_ReturnsBadRequest_WhenUserAlreadySeated()
        {
            var serviceMock = new Mock<SeatsService>();
            var controller = new SeatsController(serviceMock.Object);
            controller.ControllerContext = new ControllerContext()
            {
                HttpContext = new DefaultHttpContext()
                {
                    User = new ClaimsPrincipal(new ClaimsIdentity(new Claim[]
                    {
                        new Claim(ClaimTypes.NameIdentifier, TestUserId)
                    }))
                }
            };

            var seatNumber = 3;
            serviceMock.Setup(s => s.ReserveSeat(TestUserId, seatNumber)).Throws(new UserAlreadySeatedException());

            var actionResult = controller.ReserveSeat(seatNumber);

            var result = actionResult.Result as BadRequestResult;

            Assert.IsNotNull(result);
            serviceMock.Verify(s => s.ReserveSeat(TestUserId, seatNumber), Times.Once);
        }
    }
}