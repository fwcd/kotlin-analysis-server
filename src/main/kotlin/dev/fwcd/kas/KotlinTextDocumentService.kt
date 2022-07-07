package dev.fwcd.kas

import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.services.TextDocumentService
import org.jetbrains.kotlin.analysis.api.analyze
import org.jetbrains.kotlin.analysis.api.standalone.StandaloneAnalysisAPISession
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.incremental.ChangesCollector.Companion.getNonPrivateNames
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

/**
 * The implementation of text document-related requests, e.g. code completion etc.
 */
class KotlinTextDocumentService: TextDocumentService {
    /** The Kotlin analysis API session. */
    lateinit var session: StandaloneAnalysisAPISession

    override fun completion(position: CompletionParams?): CompletableFuture<Either<MutableList<CompletionItem>, CompletionList>> {
        // TODO: Generate proper completions
        val items = mutableListOf<CompletionItem>()
        position?.let { position ->
            // TODO: We should compare the entire path rather than just file names
            val fileName = Path.of(position.textDocument.uri).fileName.toString()
        }
        val list = CompletionList(items)
        return CompletableFuture.completedFuture(Either.forRight(list))
    }

    override fun didOpen(params: DidOpenTextDocumentParams?) {
        // TODO
    }

    override fun didChange(params: DidChangeTextDocumentParams?) {
        // TODO
    }

    override fun didClose(params: DidCloseTextDocumentParams?) {
        // TODO
    }

    override fun didSave(params: DidSaveTextDocumentParams?) {
        // TODO
    }
}