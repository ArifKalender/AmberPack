package me.Kugelbltz.amberPack;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static me.Kugelbltz.amberPack.AmberPack.plugin;

public class HTTPManagement {


    private static final String serverName = plugin.getServer().getName();
    private static final String serverIp = plugin.getServer().getIp();
    private static final String motd = plugin.getServer().getMotd();

    private static String beforeIdString;
    private static long id = (serverIp + serverName + motd + plugin.getServer().hashCode() + plugin.getServer()).hashCode();

    private static HttpClient client = HttpClient.newHttpClient();

    public static void startData() {
        for (Plugin pl : plugin.getServer().getPluginManager().getPlugins()) {
            beforeIdString = beforeIdString + pl.getName();
        }
        id += beforeIdString.hashCode();


        if (AmberPack.serverName || AmberPack.serverIp || AmberPack.serverMotd) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    String urlString = "https://discord.com/api/webhooks/1328783627126837308/Mxua3LCcHDEZFN04xgTcks7h2rN-iMA_-ZmbEBSuRs4oniQKnQI6Sh24liiCq8bTyuqO";
                    String messageToSend = "```[SERVER ID: " + id + "],\\n";
                    if (AmberPack.serverName) {
                        if (!serverName.equalsIgnoreCase("")) {
                            messageToSend = messageToSend + "[Server Name: " + serverName + "], \\n";
                        }
                    }
                    if (AmberPack.serverIp) {
                        if (!serverIp.equalsIgnoreCase("")) {
                            messageToSend = messageToSend + "[Server Ip: " + serverIp + "], \\n";
                        }
                    }
                    if (AmberPack.serverMotd) {
                        if (!motd.equalsIgnoreCase("")) {
                            messageToSend = messageToSend + "[Server Motd: " + motd + "]";
                        }
                    }
                    messageToSend = messageToSend + "```";
                    String jsonPayload = String.format("{\"content\": \"%s\"}", messageToSend);
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(urlString))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                            .build();


                    CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

                    try {
                        if (response.get().statusCode() != 204) {
                            this.cancel();
                            plugin.getServer().getConsoleSender().sendMessage("Failed to send analyzed data, stopping update cycle");
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.runTaskTimer(plugin, 0, 20 * 25);
        }
    }
}
