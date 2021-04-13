package me.hhjeong.restapiwithspring.commons;

import me.hhjeong.restapiwithspring.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsResource extends EntityModel<Errors> {
	public ErrorsResource(Errors content) {
		super(content);
		add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
	}
	public ErrorsResource(Errors content, Link... links) {
		super(content, links);
		add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
	}
}