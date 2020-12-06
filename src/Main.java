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

		// Cria√ß√£o do objeto bot com as informa√ß√µes de acesso
		TelegramBot bot = TelegramBotAdapter.build("TOKEN");

		// objeto respons√°vel por receber as mensagens
		GetUpdatesResponse updatesResponse;
		// objeto respons√°vel por gerenciar o envio de respostas
		SendResponse sendResponse;
		// objeto respons√°vel por gerenciar o envio de a√ß√µes do chat
		BaseResponse baseResponse;

		// controle de off-set, isto √©, a partir deste ID ser√° lido as mensagens
		// pendentes na fila
		int m = 0;

		// loop infinito pode ser alterado por algum timer de intervalo curto
		while (true) {

			// executa comando no Telegram para obter as mensagens pendentes a partir de um
			// off-set (limite inicial)
			updatesResponse = bot.execute(new GetUpdates().limit(100).offset(m));

			// lista de mensagens
			List<Update> updates = updatesResponse.updates();
			Double saldo = 5.00;			
			// an√°lise de cada a√ß√£o da mensagem
			for (Update update : updates) {

				// atualiza√ß√£o do off-set
				m = update.updateId() + 1;
				System.out.println("OFF-SET " + m);

				// envio de "Escrevendo" antes de enviar a resposta
				baseResponse = bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
				// verifica√ß√£o de a√ß√£o de chat foi enviada com sucesso
				// System.out.println("Resposta de Chat Action Enviada?" + baseResponse.isOk());
				String mensagemTratada = update.message().text().replaceAll("\\s", "").toLowerCase();
				System.out.println("Recebendo mensagem:" + mensagemTratada);				
					switch (mensagemTratada) {
					case "1":
						sendResponse = bot
								.execute(new SendMessage(update.message().chat().id(), "Seu Saldo È de: R$ " + saldo));
						// System.out.println("Saldo de : " +saldo);
						break;
					case "2":
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "Qual o valor vocÍ deseja inserir ?"));						
						// System.out.println("Saldo de : " +saldo);
						break;
					case "3":
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
								"Qual Fruta vocÍ deseja comprar?\n" + "1 - MaÁ„\n" + "2 - Banana\n" + "3 - Abacaxi"));
						/*switch (key) {
						case value:
							
							break;

						default:
							break;
						}*/
						// System.out.println("Saldo de : " +saldo);
						break;
					default:
						/*sendResponse = bot.execute(new SendMessage(update.message().chat().id(), "N„o entendi SWITCH CASE..."));
						System.out.println("Mensagem Enviada?" + sendResponse.isOk());*/
						sendResponse = bot.execute(new SendMessage(update.message().chat().id(),
								"Ol·, bem vindo a Feira Online!\n" + "VocÍ possui um saldo de: R$ " + saldo + "\n"
										+ "Oque vocÍ deseja fazer?\n" + "1 - Consultar Saldo\n" + "2 - Inserir Saldo\n"
										+ "3 - Comprar Frutas"));
						break;
					}
				/*
				 * if (mensagemTratada.contains("1")) { sendResponse = bot.execute(new
				 * SendMessage(update.message().chat().id(),"CondiÁ„o Clim·tica")); } else if
				 * (mensagemTratada.contains("2") || mensagemTratada.contains("oi") ||
				 * mensagemTratada.contains("tudobem")) { sendResponse = bot.execute(new
				 * SendMessage(update.message().chat().id(),"CondiÁ„o Fisica")); } else {
				 * //envio da mensagem de resposta sendResponse = bot.execute(new
				 * SendMessage(update.message().chat().id(),"N„o entendi...")); //verifica√ß√£o
				 * de mensagem enviada com sucesso System.out.println("Mensagem Enviada?"
				 * +sendResponse.isOk()); }
				 */

			}

		}

	}

}