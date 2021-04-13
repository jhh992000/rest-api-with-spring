package me.hhjeong.restapiwithspring.commons;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {
	private Link indexLink;

	public ErrorsSerializer() {

	}

	public ErrorsSerializer(Link indexLink){
		super();
		this.indexLink = indexLink;
	}

	@Override
	public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {

		gen.writeStartObject();
		gen.writeFieldName("content");

		gen.writeStartArray();
		errors.getFieldErrors().stream().forEach(e -> {
			try {
				gen.writeStartObject();
				gen.writeStringField("field", e.getField());
				gen.writeStringField("objectName", e.getObjectName());
				gen.writeStringField("code", e.getCode());
				gen.writeStringField("defaultMessage", e.getDefaultMessage());
				Object rejectedValue = e.getRejectedValue();
				if (rejectedValue != null) {
					gen.writeStringField("rejectedValue", rejectedValue.toString());
				}
				gen.writeEndObject();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
		errors.getGlobalErrors().stream().forEach(e -> {
			try {
				gen.writeStartObject();
				gen.writeStringField("objectName", e.getObjectName());
				gen.writeStringField("code", e.getCode());
				gen.writeStringField("defaultMessage", e.getDefaultMessage());
				gen.writeEndObject();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});
		gen.writeEndArray();

		gen.writeFieldName("_link");
		gen.writeStartObject();
		gen.writeStringField("index", indexLink.getHref());
		gen.writeEndObject();

		gen.writeEndObject();

	}

}
