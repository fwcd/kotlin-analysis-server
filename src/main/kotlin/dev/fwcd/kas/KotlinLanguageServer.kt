package dev.fwcd.kas

import org.eclipse.lsp4j.InitializeParams
import org.eclipse.lsp4j.InitializeResult
import org.eclipse.lsp4j.ServerCapabilities
import org.eclipse.lsp4j.ServerInfo
import org.eclipse.lsp4j.services.*
import java.util.concurrent.CompletableFuture

class KotlinLanguageServer: LanguageServer, LanguageClientAware {
    val textDocuments = KotlinTextDocumentService()
    val workspaces = KotlinWorkspaceService()

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