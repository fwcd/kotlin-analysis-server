package dev.fwcd.kas

import org.eclipse.lsp4j.DidChangeConfigurationParams
import org.eclipse.lsp4j.DidChangeWatchedFilesParams
import org.eclipse.lsp4j.services.WorkspaceService

/**
 * The implementation of workspace-related requests.
 */
class KotlinWorkspaceService: WorkspaceService {
    override fun didChangeConfiguration(params: DidChangeConfigurationParams?) {
        // TODO
    }

    override fun didChangeWatchedFiles(params: DidChangeWatchedFilesParams?) {
        // TODO
    }
}
