package dev.fwcd.kas

import org.eclipse.lsp4j.InitializeParams
import org.eclipse.lsp4j.InitializeResult
import org.eclipse.lsp4j.ServerCapabilities
import org.eclipse.lsp4j.ServerInfo
import org.eclipse.lsp4j.services.*
import java.util.concurrent.CompletableFuture

/**
 * The language server implementation, responsible for basic lifecycle management, i.e.
 * initialization and shutdown. The request implementations are handled by
 * `KotlinTextDocumentService` and `KotlinWorkspaceService`.
 */
class KotlinLanguageServer: LanguageServer, LanguageClientAware {
    /** The text document service responsible for handling code completion requests, etc. */
    val textDocuments = KotlinTextDocumentService()
    /** The text document service responsible for handling workspace updates, etc. */
    val workspaces = KotlinWorkspaceService()

    /** A proxy object for sending messages to the client. */
    private var client: LanguageClient? = null

    override fun initialize(params: InitializeParams?): CompletableFuture<InitializeResult> {
        val result = InitializeResult(
            ServerCapabilities().apply {
                // TODO
            },
            ServerInfo("Kotlin Analyzer Server")
        )
        return CompletableFuture.completedFuture(result)
    }

    override fun connect(client: LanguageClient?) {
        this.client = client
    }

    override fun shutdown(): CompletableFuture<Any> {
        return CompletableFuture.completedFuture(Unit)
    }

    override fun exit() {}

    override fun getTextDocumentService(): TextDocumentService = textDocuments

    override fun getWorkspaceService(): WorkspaceService = workspaces
}