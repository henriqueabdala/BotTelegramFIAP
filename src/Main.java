import java.util.ArrayList;
import java.util.List;

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

public class Main {

	public static void main(String[] args) {

		// CriaÃ§Ã£o do objeto bot com as informaÃ§Ãµes de acesso
		TelegramBot bot = TelegramBotAdapter.build("");

		// objeto responsÃ¡vel por receber as mensagens
		GetUpdatesResponse updatesResponse;
		// objeto responsÃ¡vel por gerenciar o envio de respostas
		SendResponse sendResponse;
		// objeto responsÃ¡vel por gerenciar o envio de aÃ§Ãµes do chat
		BaseResponse baseResponse;

		// controle de off-set, isto Ã©, a partir deste ID serÃ¡ lido as mensagens
		// pendentes na fila
		int m = 0;
		Double saldo = 5.00;
		List<String> mensagemTratada = new ArrayList<String>();
		mensagemTratada.clear();

		// loop infinito pode ser alterado por algum timer de intervalo curto
		while (true) {

			// executa comando no Telegram para obter as mensagens pendentes a partir de um
			// off-set (limite inicial)
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

			// lista de mensagens
			List<Update> updates = updatesResponse.updates();
			// anÃ¡lise de cada aÃ§Ã£o da mensagem
			for (Update update : updates) {

				// atualizaÃ§Ã£o do off-set
				m = update.updateId() + 1;
				System.out.println("OFF-SET " + m);

				// envio de "Escrevendo" antes de enviar a resposta
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				// verificaÃ§Ã£o de aÃ§Ã£o de chat foi enviada com sucesso
				// System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());
				mensagemTratada.add(update.message().text().replaceAll("\\s", "").toLowerCase());
				System.out.println("Recebendo mensagem:" + update.message().text().replaceAll("\\s", "").toLowerCase());
				System.out.println("POSICAO ZERO:" + mensagemTratada.get(0).toString());
				switch (mensagemTratada.get(0)) {
				case "1":
					sendResponse = bot
							.execute(new SendMessage(update.message().chat().id(), "Seu Saldo é de: R$ " + saldo));
					mensagemTratada.clear();
					// System.out.println("Saldo de : " +saldo);
					break;
				case "2":
					if (mensagemTratada.size() >= 2) {						
						try {
						saldo += Double.parseDouble(update.message().text().replaceAll("\\s", "").toLowerCase());
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
								"Olá, bem vindo a Feira Online!\n" + "Você possui um saldo de: R$ " + saldo + "\n"
										+ "Oque você deseja fazer?\n" + "1 - Consultar Saldo\n" + "2 - Inserir Saldo\n"
										+ "3 - Comprar Frutas"));
						mensagemTratada.clear();}
						catch (NumberFormatException e) {
							mensagemTratada.remove(mensagemTratada.size() - 1);
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Valor Inválido !"));
						}
					} else {						
						mensagemTratada.add("SOMASALDO");
						sendResponse = bot.execute(
								new SendMessage(update.message().chat().id(), "Qual o valor você deseja inserir ?"));						
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
											"Olá, bem vindo a Feira Online!\n" + "Você possui um saldo de: R$ " + saldo
													+ "\n" + "Oque você deseja fazer?\n" + "1 - Consultar Saldo\n"
													+ "2 - Inserir Saldo\n" + "3 - Comprar Frutas"));
									mensagemTratada.clear();
								} else {
									sendResponse = bot
											.execute(new SendMessage(update.message().chat().id(), "Saldo Insuficiente!"));
									sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
											"Olá, bem vindo a Feira Online!\n" + "Você possui um saldo de: R$ " + saldo
													+ "\n" + "Oque você deseja fazer?\n" + "1 - Consultar Saldo\n"
													+ "2 - Inserir Saldo\n" + "3 - Comprar Frutas"));
									mensagemTratada.clear();
								}
								break;
							default:
								sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
										"Olá, bem vindo a Feira Online!\n" + "Você possui um saldo de: R$ " + saldo
												+ "\n" + "Oque você deseja fazer?\n" + "1 - Consultar Saldo\n"
												+ "2 - Inserir Saldo\n" + "3 - Comprar Frutas"));
								mensagemTratada.clear();
								break;
							}								
							}else {
								sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
										"A Maçã custa R$ 1,00, deseja comprar mesmo assim?\n" + "1 - Sim\n" + "2 - Não"));
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
												"Olá, bem vindo a Feira Online!\n" + "Você possui um saldo de: R$ " + saldo
														+ "\n" + "Oque você deseja fazer?\n" + "1 - Consultar Saldo\n"
														+ "2 - Inserir Saldo\n" + "3 - Comprar Frutas"));
										mensagemTratada.clear();
									} else {
										sendResponse = bot
												.execute(new SendMessage(update.message().chat().id(), "Saldo Insuficiente!"));
										sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
												"Olá, bem vindo a Feira Online!\n" + "Você possui um saldo de: R$ " + saldo
														+ "\n" + "Oque você deseja fazer?\n" + "1 - Consultar Saldo\n"
														+ "2 - Inserir Saldo\n" + "3 - Comprar Frutas"));
										mensagemTratada.clear();
									}
									break;
								default:
									sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
											"Olá, bem vindo a Feira Online!\n" + "Você possui um saldo de: R$ " + saldo
													+ "\n" + "Oque você deseja fazer?\n" + "1 - Consultar Saldo\n"
													+ "2 - Inserir Saldo\n" + "3 - Comprar Frutas"));
									mensagemTratada.clear();
									break;
								}								
								}else {
									sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
											"A Banana custa R$ 3,00, deseja comprar mesmo assim?\n" + "1 - Sim\n" + "2 - Não"));
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
												"Olá, bem vindo a Feira Online!\n" + "Você possui um saldo de: R$ " + saldo
														+ "\n" + "Oque você deseja fazer?\n" + "1 - Consultar Saldo\n"
														+ "2 - Inserir Saldo\n" + "3 - Comprar Frutas"));
										mensagemTratada.clear();
									} else {
										sendResponse = bot
												.execute(new SendMessage(update.message().chat().id(), "Saldo Insuficiente!"));
										sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
												"Olá, bem vindo a Feira Online!\n" + "Você possui um saldo de: R$ " + saldo
														+ "\n" + "Oque você deseja fazer?\n" + "1 - Consultar Saldo\n"
														+ "2 - Inserir Saldo\n" + "3 - Comprar Frutas"));
										mensagemTratada.clear();
									}
									break;
								default:
									sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
											"Olá, bem vindo a Feira Online!\n" + "Você possui um saldo de: R$ " + saldo
													+ "\n" + "Oque você deseja fazer?\n" + "1 - Consultar Saldo\n"
													+ "2 - Inserir Saldo\n" + "3 - Comprar Frutas"));
									mensagemTratada.clear();
									break;
								}								
								}else {
									sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
											"A Abacaxi custa R$ 5,00, deseja comprar mesmo assim?\n" + "1 - Sim\n" + "2 - Não"));
									mensagemTratada.add("SELECIONADAFRUTA");
								}
							break;
						default:
							sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
									"Olá, bem vindo a Feira Online!\n" + "Você possui um saldo de: R$ " + saldo + "\n"
											+ "Oque você deseja fazer?\n" + "1 - Consultar Saldo\n"
											+ "2 - Inserir Saldo\n" + "3 - Comprar Frutas"));
							mensagemTratada.clear();
							break;
						}
					} else {
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
								"Qual Fruta você deseja comprar?\n" + "1 - Maçã\n" + "2 - Banana\n" + "3 - Abacaxi"));
						mensagemTratada.add("REALIZACOMPRA");
					}
					break;
				default:
					/*
					 * sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
					 * "Não entendi SWITCH CASE...")); System.out.println("Mensagem Enviada?" +
					 * sendResponse.isOk());
					 */
					sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
							"Olá, bem vindo a Feira Online!\n" + "Você possui um saldo de: R$ " + saldo + "\n"
									+ "Oque você deseja fazer?\n" + "1 - Consultar Saldo\n" + "2 - Inserir Saldo\n"
									+ "3 - Comprar Frutas"));
					mensagemTratada.clear();
					break;
				}
				/*
				 * if (mensagemTratada.contains("1")) { sendResponse = bot.execute(new
				 * SendMessage(update.message().chat().id(),"Condição Climática")); } else if
				 * (mensagemTratada.contains("2") || mensagemTratada.contains("oi") ||
				 * mensagemTratada.contains("tudobem")) { sendResponse = bot.execute(new
				 * SendMessage(update.message().chat().id(),"Condição Fisica")); } else {
				 * //envio da mensagem de resposta sendResponse = bot.execute(new
				 * SendMessage(update.message().chat().id(),"Não entendi...")); //verificaÃ§Ã£o
				 * de mensagem enviada com sucesso System.out.println("Mensagem Enviada?"
				 * +sendResponse.isOk()); }
				 */

			}

		}

	}

}