package dev.fwcd.kas

import com.intellij.openapi.vfs.StandardFileSystems
import com.intellij.psi.PsiManager
import org.eclipse.lsp4j.*
import org.eclipse.lsp4j.jsonrpc.messages.Either
import org.eclipse.lsp4j.services.TextDocumentService
import org.jetbrains.kotlin.analysis.api.analyze
import org.jetbrains.kotlin.analysis.api.standalone.StandaloneAnalysisAPISession
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.incremental.ChangesCollector.Companion.getNonPrivateNames
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.psiUtil.getFileOrScriptDeclarations
import java.net.URI
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

/**
 * The implementation of text document-related requests, e.g. code completion etc.
 */
class KotlinTextDocumentService: TextDocumentService {
    /** The Kotlin analysis API session. */
    lateinit var session: StandaloneAnalysisAPISession

    /** Looks up a KtFile (the AST) for a URI via PsiManager. */
    private fun findKtFile(uri: String): KtFile? {
        val fs = StandardFileSystems.local()
        val psiManager = PsiManager.getInstance(session.project)
        val path = Path.of(URI(uri))
        val vFile = fs.findFileByPath(path.toString())
        val psiFile = vFile?.let(psiManager::findFile)
        return psiFile as? KtFile
    }

    override fun completion(position: CompletionParams?): CompletableFuture<Either<MutableList<CompletionItem>, CompletionList>> {
        val items = mutableListOf<CompletionItem>()

        position
            ?.let { findKtFile(it.textDocument.uri) }
            ?.also { ktFile ->
                // TODO: Proper completions, also figure out how the analysis API might be useful here (analyze { ... })
                for (decl in ktFile.getFileOrScriptDeclarations()) {
                    items.add(CompletionItem(decl.name))
                }
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