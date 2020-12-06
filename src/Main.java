import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {
		JSONObject jsonObject;

		// Criacao do objeto bot com as informacoes de acesso
		TelegramBot bot = TelegramBotAdapter.build("");

		// objeto responsavel por receber as mensagens
		GetUpdatesResponse updatesResponse;
		// objeto responsavel por gerenciar o envio de respostas
		SendResponse sendResponse;
		// objeto responsavel por gerenciar o envio de acoes do chat
		BaseResponse baseResponse;

		// controle de off-set, isto �, a partir deste ID sera lido as mensagens
		// pendentes na fila
		int m = 0;
		//Saldo Inicial para compras na Feira Online
		Double saldo = 5.00;		
		/* Exemplifica��o do funcionamento:
		 * A partir de qualquer comando ser� aberto um menu onde ja existem as op��es para sele��o
		 * caso n�o selecione nenhuma das op��es em quest�o retorna ao menu inferior ou Inicial	 
		 */
		//StringList para tratamento das op��es selecionadas pelo usu�rio
		List<String> mensagemTratada = new ArrayList<String>();
		mensagemTratada.clear();

		// loop infinito pode ser alterado por algum timer de intervalo curto
		while (true) {

			// executa comando no Telegram para obter as mensagens pendentes a partir de um
			// off-set (limite inicial)
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

			// lista de mensagens
			List<Update> updates = updatesResponse.updates();
			for (Update update : updates) {

				// atualiza��o do off-set
				m = update.updateId() + 1;
				System.out.println("OFF-SET " + m);

				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));				
				mensagemTratada.add(update.message().text().replaceAll("\\s", "").toLowerCase());
				switch (mensagemTratada.get(0)) {
				case "1":
					sendResponse = bot
							.execute(new SendMessage(update.message().chat().id(), "Seu Saldo � de: R$ " + saldo));
					mensagemTratada.clear();
					break;
				case "2":
					if (mensagemTratada.size() >= 2) {						
						try {
						saldo += Double.parseDouble(update.message().text().replaceAll("\\s", "").toLowerCase());
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
								"Ol�, bem vindo a Feira Online!\n" + "Voc� possui um saldo de: R$ " + saldo + "\n"
										+ "Oque voc� deseja fazer?\n" + "1 - Consultar Saldo\n" + "2 - Inserir Saldo\n"
										+ "3 - Comprar Frutas\n" + "4 - Clima Atual"));
						mensagemTratada.clear();}
						catch (NumberFormatException e) {
							mensagemTratada.remove(mensagemTratada.size() - 1);
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Valor Inv�lido !"));
							sendResponse = bot.execute(
									new SendMessage(update.message().chat().id(), "Qual o valor voc� deseja inserir ?"));
						}
					} else {						
						mensagemTratada.add("SOMASALDO");
						sendResponse = bot.execute(
								new SendMessage(update.message().chat().id(), "Qual o valor voc� deseja inserir ?"));						
					}
					break;
				case "3":
					System.out.println("size " + mensagemTratada.size());
					if (mensagemTratada.size() >= 2) {
						switch (mensagemTratada.get(2)) {
						case "1":
							if (mensagemTratada.size() >= 4) {
							 switch (mensagemTratada.get(4)) {
							case "1":
								if (saldo >= 1) {
									saldo -= 1;
									sendResponse = bot.execute(
											new SendMessage(update.message().chat().id(), "Compra Realizada com Sucesso!"));
									sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
											"Ol�, bem vindo a Feira Online!\n" + "Voc� possui um saldo de: R$ " + saldo + "\n"
													+ "Oque voc� deseja fazer?\n" + "1 - Consultar Saldo\n" + "2 - Inserir Saldo\n"
													+ "3 - Comprar Frutas\n" + "4 - Clima Atual"));
									mensagemTratada.clear();
								} else {
									sendResponse = bot
											.execute(new SendMessage(update.message().chat().id(), "Saldo Insuficiente!"));
									sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
											"Ol�, bem vindo a Feira Online!\n" + "Voc� possui um saldo de: R$ " + saldo + "\n"
													+ "Oque voc� deseja fazer?\n" + "1 - Consultar Saldo\n" + "2 - Inserir Saldo\n"
													+ "3 - Comprar Frutas\n" + "4 - Clima Atual"));
									mensagemTratada.clear();
								}
								break;
							default:
								mensagemTratada.remove(mensagemTratada.size() - 1);
								sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Op��o Inv�lida !"));
								sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
										"A Ma�� custa R$ 1,00, deseja comprar mesmo assim?\n" + "1 - Sim\n" + "2 - N�o"));
								break;
							}								
							}else {
								sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
										"A Ma�� custa R$ 1,00, deseja comprar mesmo assim?\n" + "1 - Sim\n" + "2 - N�o"));
								mensagemTratada.add("SELECIONADAFRUTA");
							}												
							break;
						case "2":
							if (mensagemTratada.size() >= 4) {
								 switch (mensagemTratada.get(4)) {
								case "1":
									if (saldo >= 3) {
										saldo -= 3;
										sendResponse = bot.execute(
												new SendMessage(update.message().chat().id(), "Compra Realizada com Sucesso!"));
										sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
												"Ol�, bem vindo a Feira Online!\n" + "Voc� possui um saldo de: R$ " + saldo + "\n"
														+ "Oque voc� deseja fazer?\n" + "1 - Consultar Saldo\n" + "2 - Inserir Saldo\n"
														+ "3 - Comprar Frutas\n" + "4 - Clima Atual"));
										mensagemTratada.clear();
									} else {
										sendResponse = bot
												.execute(new SendMessage(update.message().chat().id(), "Saldo Insuficiente!"));
										sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
												"Ol�, bem vindo a Feira Online!\n" + "Voc� possui um saldo de: R$ " + saldo + "\n"
														+ "Oque voc� deseja fazer?\n" + "1 - Consultar Saldo\n" + "2 - Inserir Saldo\n"
														+ "3 - Comprar Frutas\n" + "4 - Clima Atual"));
										mensagemTratada.clear();
									}
									break;
								default:
									mensagemTratada.remove(mensagemTratada.size() - 1);
									sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Op��o Inv�lida !"));
									sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
											"A Banana custa R$ 3,00, deseja comprar mesmo assim?\n" + "1 - Sim\n" + "2 - N�o"));
									break;
								}								
								}else {
									sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
											"A Banana custa R$ 3,00, deseja comprar mesmo assim?\n" + "1 - Sim\n" + "2 - N�o"));
									mensagemTratada.add("SELECIONADAFRUTA");
								}
							break;
						case "3":
							if (mensagemTratada.size() >= 4) {
								 switch (mensagemTratada.get(4)) {
								case "1":
									if (saldo >= 5) {
										saldo -= 5;
										sendResponse = bot.execute(
												new SendMessage(update.message().chat().id(), "Compra Realizada com Sucesso!"));
										sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
												"Ol�, bem vindo a Feira Online!\n" + "Voc� possui um saldo de: R$ " + saldo + "\n"
														+ "Oque voc� deseja fazer?\n" + "1 - Consultar Saldo\n" + "2 - Inserir Saldo\n"
														+ "3 - Comprar Frutas\n" + "4 - Clima Atual"));
										mensagemTratada.clear();
									} else {
										sendResponse = bot
												.execute(new SendMessage(update.message().chat().id(), "Saldo Insuficiente!"));
										sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
												"Ol�, bem vindo a Feira Online!\n" + "Voc� possui um saldo de: R$ " + saldo + "\n"
														+ "Oque voc� deseja fazer?\n" + "1 - Consultar Saldo\n" + "2 - Inserir Saldo\n"
														+ "3 - Comprar Frutas\n" + "4 - Clima Atual"));
										mensagemTratada.clear();
									}
									break;
								default:
									mensagemTratada.remove(mensagemTratada.size() - 1);
									sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Op��o Inv�lida !"));
									sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
											"A Abacaxi custa R$ 5,00, deseja comprar mesmo assim?\n" + "1 - Sim\n" + "2 - N�o"));
									break;
								}								
								}else {
									sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
											"A Abacaxi custa R$ 5,00, deseja comprar mesmo assim?\n" + "1 - Sim\n" + "2 - N�o"));
									mensagemTratada.add("SELECIONADAFRUTA");
								}
							break;
						default:
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
									"Ol�, bem vindo a Feira Online!\n" + "Voc� possui um saldo de: R$ " + saldo + "\n"
											+ "Oque voc� deseja fazer?\n" + "1 - Consultar Saldo\n"
											+ "2 - Inserir Saldo\n" + "3 - Comprar Frutas"));
							mensagemTratada.clear();
							break;
						}
					} else {
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
								"Qual Fruta voc� deseja comprar?\n" + "1 - Ma��\n" + "2 - Banana\n" + "3 - Abacaxi\n" + "4 - Menu Inicial"));
						mensagemTratada.add("REALIZACOMPRA");
					}
					break;
				case "4":
					OkHttpClient client = new OkHttpClient().newBuilder()
					  .build();
					Request request = new Request.Builder()
					  .url("")
					  .method("GET", null)
					  .build();
					Response response = client.newCall(request).execute();
					//new Gson().fromJson(response.body().string(), 
					JSONParser parser = new JSONParser();
					
					jsonObject = (JSONObject) parser.parse(response.body().string());
					JSONObject localizao = (JSONObject) jsonObject.get("location");
					JSONObject atual = (JSONObject) jsonObject.get("current");
					
					String nome = (String) localizao.get("name");
					String horaLocal = (String) localizao.get("localtime");
					String temperatura = (String) atual.get("temp_c").toString();
					
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "A cidade de: " + nome + ", data/hora: " + horaLocal + ", temperatura: " + temperatura + " graus celsius" ));
					/*sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
							"Ol�, bem vindo a Feira Online!\n" + "Voc� possui um saldo de: R$ " + saldo + "\n"
									+ "Oque voc� deseja fazer?\n" + "1 - Consultar Saldo\n" + "2 - Inserir Saldo\n"
									+ "3 - Comprar Frutas\n" + "4 - Clima Atual"));*/
					mensagemTratada.clear();
					break;
				default:	
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
							"Ol�, bem vindo a Feira Online!\n" + "Voc� possui um saldo de: R$ " + saldo + "\n"
									+ "Oque voc� deseja fazer?\n" + "1 - Consultar Saldo\n" + "2 - Inserir Saldo\n"
									+ "3 - Comprar Frutas\n" + "4 - Clima Atual"));
					mensagemTratada.clear();
					break;
				}
			}

		}

	}

}