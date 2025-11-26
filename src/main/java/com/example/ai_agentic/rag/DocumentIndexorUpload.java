package com.example.ai_agentic.rag;

import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class DocumentIndexorUpload {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(DocumentIndexorUpload.class);

    @Value("classpath:/pdfs/cv.pdf")
    private Resource documentResource;

    @Value("${vector.store.filename:store.json}")
    private String fileStore;

    private final SimpleVectorStore vectorStore;


    public DocumentIndexorUpload(SimpleVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }


    /**
     * Méthode pour charger et indexer un fichier PDF
     * @param pdfFile
     * @throws IOException
     */
    public void loadFile(MultipartFile pdfFile) throws IOException {
        Path storePath = Path.of("src", "main", "resources", "store");

        try {
            // Correction: il manquait une parenthèse fermante
            if (!Files.exists(storePath)) {
                Files.createDirectories(storePath);
                log.info("✅ Répertoire créé: {}", storePath.toAbsolutePath());
            }

            // Correction: utiliser 'file' au lieu de 'storeFile'
            File file = new File(storePath.toFile(), this.fileStore);
            log.info("📁 Chemin du VectorStore: {}", file.getAbsolutePath());

            if (!file.exists()) {
                log.info("📄 Indexation initiale du PDF en cours...");

                PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(pdfFile.getResource());
                List<Document> documents = pdfDocumentReader.get();
                log.info("📖 {} pages extraites du PDF", documents.size());

                TextSplitter textSplitter = new TokenTextSplitter();
                List<Document> chunks = textSplitter.apply(documents);
                log.info("✂️ {} chunks créés", chunks.size());

                // Ajouter au vector store et générer les embeddings
                log.info("🔄 Génération des embeddings...");
                vectorStore.add(chunks);

                // Sauvegarder
                log.info("💾 Sauvegarde du VectorStore...");
                vectorStore.save(file);
                log.info("✅ VectorStore sauvegardé avec succès");
            } else {
                log.info("📂 Chargement du VectorStore existant...");
                vectorStore.load(file);
                log.info("✅ VectorStore chargé avec succès");
            }

        } catch (IOException e) {
            log.error("❌ Erreur lors de la création/chargement du VectorStore", e);
            throw new RuntimeException("Impossible de créer le VectorStore", e);
        }
    }
}