package com.example.ai_agentic.rag;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
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
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class DocumentIndexor {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(DocumentIndexor.class);
    @Value("classpath:/pdfs/cv.pdf")
    private Resource documentResource;
    @Value("${vector.store.filename:store.json}")
    private String fileStore;

    @Bean
    public SimpleVectorStore getVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        Path storePath = Path.of("src", "main", "resources", "store");

        try {
            if (!Files.exists(storePath, new LinkOption[0])) {
                Files.createDirectories(storePath);
                log.info("✅ Répertoire créé: {}", storePath.toAbsolutePath());
            }

            File file = new File(storePath.toFile(), this.fileStore);
            log.info("\ud83d\udcc1 Chemin du VectorStore: {}", file.getAbsolutePath());
            if (!file.exists()) {
                log.info("📄 Indexation initiale du PDF en cours...");
                PagePdfDocumentReader pdfDocumentReader = new PagePdfDocumentReader(this.documentResource);
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
                log.info("\ud83d\udcc2 Chargement du VectorStore existant...");
                vectorStore.load(file);
                log.info("✅ VectorStore chargé avec succès");
            }

            return vectorStore;
        } catch (IOException e) {
            log.error("❌ Erreur lors de la création/chargement du VectorStore", e);
            throw new RuntimeException("Impossible de créer le VectorStore", e);
        }
    }
}

