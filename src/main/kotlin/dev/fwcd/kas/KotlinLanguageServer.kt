package dev.fwcd.kas

import com.intellij.mock.MockApplication
import com.intellij.mock.MockProject
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Disposer
import org.eclipse.lsp4j.InitializeParams
import org.eclipse.lsp4j.InitializeResult
import org.eclipse.lsp4j.ServerCapabilities
import org.eclipse.lsp4j.ServerInfo
import org.eclipse.lsp4j.services.*
import org.jetbrains.kotlin.analysis.api.standalone.configureApplicationEnvironment
import org.jetbrains.kotlin.analysis.api.standalone.configureProjectEnvironment
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.config.addKotlinSourceRoots
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.config.CompilerConfiguration
import java.net.URI
import java.nio.file.Path
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
        // TODO: Investigate proper lifecycle management with disposables (should we store a Disposable in the class?)
        // TODO: Make source-resolution more flexible (currently only Gradle-style src/main/kotlin folders are considered)
        // TODO: Add proper message collector (e.g. one that logs the messages)?

        // Locate sources
        val workspaceFolders = params?.workspaceFolders ?: listOf()
        val sourceRoots = workspaceFolders
            .map { Path.of(URI(it.uri)).resolve("src").resolve("main").resolve("kotlin").toString() }

        // Configure Kotlin compiler
        val compilerConfig = CompilerConfiguration()
        compilerConfig.addKotlinSourceRoots(sourceRoots)
        compilerConfig.put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)

        // Bootstrap Kotlin compiler application environment for standalone analysis mode
        val application = ApplicationManager.getApplication() as MockApplication
        configureApplicationEnvironment(application)

        // Bootstrap Kotlin core environment for standalone analysis mode
        val coreEnv = KotlinCoreEnvironment.createForProduction(
            Disposer.newDisposable(),
            compilerConfig,
            EnvironmentConfigFiles.JVM_CONFIG_FILES
        )
        configureProjectEnvironment(
            coreEnv.project as MockProject,
            compilerConfig,
            coreEnv::createPackagePartProvider
        )

        // Assemble LSP initialization response
        val result = InitializeResult(
            ServerCapabilities().apply {
                // TODO
            },
            ServerInfo("Kotlin Analysis Server")
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