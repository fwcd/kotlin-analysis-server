package dev.fwcd.kas

import org.eclipse.lsp4j.jsonrpc.Launcher
import org.eclipse.lsp4j.launch.LSPLauncher
import org.eclipse.lsp4j.services.LanguageClient

fun main() {
    // Bootstrap the language server
    val server = KotlinLanguageServer()
    val launcher: Launcher<LanguageClient> = LSPLauncher.createServerLauncher(
        server,
        System.`in`,
        System.out
    )

    // Inject the client proxy and start the language server
    server.connect(launcher.remoteProxy)
    launcher.startListening()
}
