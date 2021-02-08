package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.entity.editor.project.PreviewRenderer;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;

public class GraphPreviewRenderer implements PreviewRenderer, Disposable {
    private PipelineRenderer pipelineRenderer;
    private Texture renderToTexture;
    private RenderOutputs.RenderToTexture renderOutput;

    public GraphPreviewRenderer(PipelineRenderer pipelineRenderer) {
        this.pipelineRenderer = pipelineRenderer;
        renderOutput = new RenderOutputs.RenderToTexture(null);
    }

    public void prepare(int width, int height) {
        if (width > 0 && height > 0) {
            if (renderToTexture == null ||
                    renderToTexture.getWidth() != width || renderToTexture.getHeight() != height) {
                if (renderToTexture != null)
                    renderToTexture.dispose();
                renderToTexture = new Texture(width, height, Pixmap.Format.RGB888);
                renderOutput.setTexture(renderToTexture);
            }
            pipelineRenderer.render(renderOutput);
        }
    }

    @Override
    public void render(Batch batch, float x, float y, float width, float height) {
        if (renderToTexture != null) {
            batch.draw(renderToTexture, x, y, width, height);
        }
    }

    @Override
    public void dispose() {
        if (renderToTexture != null) {
            renderToTexture.dispose();
        }
        pipelineRenderer.dispose();
    }
}
