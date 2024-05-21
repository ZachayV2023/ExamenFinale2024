package org.kickmyb.server.task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ControllerTask {

	@Autowired 		private ServiceTask serviceTask;

	@GetMapping("/api/final/1/{nombre}")
	public @ResponseBody int calcul(
			@PathVariable int nombre) throws NotMultiple {
		System.out.println("REÇU UNE REQUÊTE POUR CALCUL");
		return serviceTask.calcul(nombre);
	}

	@GetMapping("/api/final/0")
	public @ResponseBody String attente()  {
		System.out.println("REÇU UNE REQUÊTE POUR ATTENTE");
		serviceTask.attente();
		return "";
	}

}
