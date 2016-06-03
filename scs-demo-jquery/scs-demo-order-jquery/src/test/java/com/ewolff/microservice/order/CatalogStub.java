package com.ewolff.microservice.order;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Profile("test")
public class CatalogStub {

	@RequestMapping("/catalog/item-choice.snippet")
	@ResponseBody
	public String itemChoice(@RequestParam(name = "selected", required = false) Long selected,
			@RequestParam("name") String name, @RequestParam("id") String id) {
		return String.format("<select id=\"%s\" name=\"%s\">" + "<option value=\"1\">iPod</option>" + "</select>", id,
				name);

	}
	
	
	@RequestMapping("/catalog/{id}.snippet")
	@ResponseBody
	public String itemSnippet(@PathVariable("id") long id) {
		return String.format("This is item "+id);

	}

}
