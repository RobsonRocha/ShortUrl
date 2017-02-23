package br.com.shorturl.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.h2.tools.DeleteDbFiles;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import br.com.shorturl.controller.MainController;
import br.com.shorturl.pojo.ShortURL;
import br.com.shorturl.pojo.Top10;
import br.com.shorturl.utils.Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class MainTest {

	private static MockMvc mockMvc;

	@BeforeClass
	public static void setup() throws SQLException, PropertyVetoException {
		DeleteDbFiles.execute("~", "test", true);
		Utils.createTable();
		mockMvc = standaloneSetup(new MainController()).build();
	}

	@Test
	public void testEncodeURLWithoutAlias() throws Exception {

		String url = Utils.randonWord();

		MvcResult result = mockMvc
				.perform(
						put("/encode?originalurl=" + url).contentType(
								MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated()).andReturn();

		Gson gson = new Gson();
		ShortURL answer = gson.fromJson(result.getResponse()
				.getContentAsString(), new TypeToken<ShortURL>() {
		}.getType());

		Assert.assertTrue(answer.getUrl().equals(
				"http://shortener.be/" + answer.getAlias()));
		Assert.assertTrue(answer.getStatistics().getTimeTaken() >= 0);

		result = mockMvc
				.perform(
						put("/decode?shorturl=" + answer.getUrl()).accept(
								MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();

		gson = new Gson();
		ShortURL answer2 = gson.fromJson(result.getResponse()
				.getContentAsString(), new TypeToken<ShortURL>() {
		}.getType());

		Assert.assertTrue(answer.getAlias().equals(answer2.getAlias()));
		Assert.assertTrue(url.equals(answer2.getUrl()));
		Assert.assertTrue(answer2.getStatistics().getTimeTaken() >= 0);
	}

	@Test
	public void testEncodeURLWithAlias() throws Exception {

		String url = Utils.randonWord();

		MvcResult result = mockMvc
				.perform(
						put("/encode?originalurl=" + url + "&alias=TeStE2")
								.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated()).andReturn();

		Gson gson = new Gson();
		ShortURL answer = gson.fromJson(result.getResponse()
				.getContentAsString(), new TypeToken<ShortURL>() {
		}.getType());

		Assert.assertTrue(answer.getUrl().equals(
				"http://shortener.be/" + answer.getAlias()));
		Assert.assertTrue(answer.getStatistics().getTimeTaken() >= 0);

		result = mockMvc
				.perform(
						put("/decode?shorturl=" + answer.getUrl()).accept(
								MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();

		gson = new Gson();
		ShortURL answer2 = gson.fromJson(result.getResponse()
				.getContentAsString(), new TypeToken<ShortURL>() {
		}.getType());

		Assert.assertTrue(answer.getAlias().equals(answer2.getAlias()));
		Assert.assertTrue(url.equals(answer2.getUrl()));
		Assert.assertTrue(answer2.getStatistics().getTimeTaken() >= 0);
	}

	@Test
	public void testEncodeEmptyURL() throws Exception {

		mockMvc.perform(
				put("/encode").contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testEncodeWithoutParam() throws Exception {

		String url = Utils.randonWord();

		mockMvc.perform(
				put("/encode/" + url + ".fasfsd").contentType(
						MediaType.APPLICATION_JSON_UTF8)).andExpect(
				status().isNotFound());
	}

	@Test
	public void testEncodeWithoutParam2() throws Exception {

		String url = Utils.randonWord();

		mockMvc.perform(
				put("/encode?alias=" + url + ".fasfsd").contentType(
						MediaType.APPLICATION_JSON_UTF8)).andExpect(
				status().isBadRequest());
	}

	@Test
	public void testGetWithoutParam() throws Exception {

		String url = Utils.randonWord();

		mockMvc.perform(
				put("/decode/" + url).accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testGetInvalidURL() throws Exception {

		String url = Utils.randonWord();

		MvcResult result = mockMvc
				.perform(
						put(
								"/decode?shorturl=fasfafshttp://shortener.be/"
										+ url).accept(
								MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotAcceptable()).andReturn();

		Gson gson = new Gson();
		ShortURL answer = gson.fromJson(result.getResponse()
				.getContentAsString(), new TypeToken<ShortURL>() {
		}.getType());

		Assert.assertTrue(!answer.getErrorCode().isEmpty());
		Assert.assertTrue(answer.getDescription().equals(
				"Invalid URL fasfafshttp://shortener.be/" + url));
		Assert.assertTrue(answer.getStatistics().getTimeTaken() >= 0);
	}

	@Test
	public void testGetURLThatDoesNotExist() throws Exception {

		String url = Utils.randonWord();

		MvcResult result = mockMvc
				.perform(
						put("/decode?shorturl=http://shortener.be/1" + url)
								.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound()).andReturn();

		Gson gson = new Gson();
		ShortURL answer = gson.fromJson(result.getResponse()
				.getContentAsString(), new TypeToken<ShortURL>() {
		}.getType());

		Assert.assertTrue(!answer.getErrorCode().isEmpty());
		Assert.assertTrue(answer.getDescription().equals(
				"Short URL not found http://shortener.be/1" + url));
		Assert.assertTrue(answer.getStatistics().getTimeTaken() >= 0);
	}

	@Test
	public void testEncodeURLWithRepetedAlias() throws Exception {

		String url = Utils.randonWord();

		MvcResult result = mockMvc
				.perform(
						put("/encode?originalurl=" + url + "&alias=TeStE")
								.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated()).andReturn();

		Gson gson = new Gson();
		ShortURL answer = gson.fromJson(result.getResponse()
				.getContentAsString(), new TypeToken<ShortURL>() {
		}.getType());

		Assert.assertTrue(answer.getUrl().equals(
				"http://shortener.be/" + answer.getAlias()));
		Assert.assertTrue(answer.getStatistics().getTimeTaken() >= 0);

		result = mockMvc
				.perform(
						put("/decode?shorturl=" + answer.getUrl()).accept(
								MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk()).andReturn();

		gson = new Gson();
		ShortURL answer2 = gson.fromJson(result.getResponse()
				.getContentAsString(), new TypeToken<ShortURL>() {
		}.getType());

		Assert.assertTrue(answer.getAlias().equals(answer2.getAlias()));
		Assert.assertTrue(url.equals(answer2.getUrl()));
		Assert.assertTrue(answer2.getStatistics().getTimeTaken() >= 0);

		result = mockMvc
				.perform(
						put("/encode?originalurl=fsdfsfs&alias=TeStE")
								.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isConflict()).andReturn();

		gson = new Gson();
		ShortURL answer3 = gson.fromJson(result.getResponse()
				.getContentAsString(), new TypeToken<ShortURL>() {
		}.getType());

		Assert.assertTrue(!answer3.getErrorCode().isEmpty());
		Assert.assertTrue(answer3.getDescription().equals(
				"CUSTOM ALIAS ALREADY EXISTS"));
		Assert.assertTrue(answer3.getStatistics().getTimeTaken() >= 0);

	}

	@Test
	public void testTop10() throws Exception {

		List<String> urls = new ArrayList<String>();

		for (int i = 0; i < 10; i++) {
			String url = Utils.randonWord();

			MvcResult result = mockMvc
					.perform(
							put("/encode?originalurl=" + url).contentType(
									MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isCreated()).andReturn();

			Gson gson = new Gson();
			ShortURL answer = gson.fromJson(result.getResponse()
					.getContentAsString(), new TypeToken<ShortURL>() {
			}.getType());

			urls.add(answer.getUrl());
		}

		for (int i = 0; i < 20; i++) {
			int random = ((int) (Math.random() * 9)) + 1;

			mockMvc.perform(
					put("/decode?shorturl=" + urls.get(random)).accept(
							MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isOk()).andReturn();

		}

		MvcResult result = mockMvc
				.perform(
						get("/top10").contentType(
								MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated()).andReturn();

		Gson gson = new Gson();
		List<Top10> answer = gson.fromJson(result.getResponse()
				.getContentAsString(), new TypeToken<List<Top10>>() {
		}.getType());

		Assert.assertTrue(answer.size() == 10);
		Top10 t = answer.get(0);
		Assert.assertTrue(urls.contains("http://shortener.be/"
				+ t.getShortUrl().getAlias()));
	}

	@Test
	public void testTop10WithAlias() throws Exception {

		Utils.delete();

		List<String> urls = new ArrayList<String>();

		for (int i = 0; i < 10; i++) {
			String url = Utils.randonWord();
			String alias = Utils.randonWord();

			MvcResult result = mockMvc
					.perform(
							put(
									"/encode?originalurl=" + url + "&alias="
											+ alias).contentType(
									MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isCreated()).andReturn();

			Gson gson = new Gson();
			ShortURL answer = gson.fromJson(result.getResponse()
					.getContentAsString(), new TypeToken<ShortURL>() {
			}.getType());

			urls.add(answer.getUrl());
		}

		for (int i = 0; i < 20; i++) {
			int random = ((int) (Math.random() * 9)) + 1;

			mockMvc.perform(
					put("/decode?shorturl=" + urls.get(random)).accept(
							MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isOk()).andReturn();

		}

		MvcResult result = mockMvc
				.perform(
						get("/top10").contentType(
								MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated()).andReturn();

		Gson gson = new Gson();
		List<Top10> answer = gson.fromJson(result.getResponse()
				.getContentAsString(), new TypeToken<List<Top10>>() {
		}.getType());

		Assert.assertTrue(answer.size() == 10);
		Top10 t = answer.get(0);
		Assert.assertTrue(urls.contains("http://shortener.be/"
				+ t.getShortUrl().getAlias()));
	}

	@Test
	public void testShortAShortedURL() throws Exception {

		String url = Utils.randonWord();

		MvcResult result = mockMvc
				.perform(
						put("/encode?originalurl=" + url).contentType(
								MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated()).andReturn();

		Gson gson = new Gson();
		ShortURL answer = gson.fromJson(result.getResponse()
				.getContentAsString(), new TypeToken<ShortURL>() {
		}.getType());

		Assert.assertTrue(answer.getUrl().equals(
				"http://shortener.be/" + answer.getAlias()));
		Assert.assertTrue(answer.getStatistics().getTimeTaken() >= 0);
		
		result = mockMvc
				.perform(
						put("/encode?originalurl=" + answer.getUrl()).contentType(
								MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isNotAcceptable()).andReturn();
		
		ShortURL answer1 = gson.fromJson(result.getResponse()
				.getContentAsString(), new TypeToken<ShortURL>() {
		}.getType());
		
		Assert.assertTrue(!answer1.getErrorCode().isEmpty());
		Assert.assertTrue(answer1.getDescription().equals(
				"Unable to short a shorted URL "+answer1.getUrl()));
		Assert.assertTrue(answer1.getStatistics().getTimeTaken() >= 0);
		
	}

}
