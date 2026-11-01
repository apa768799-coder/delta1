// Замени метод render в файле Panel.java на этот
public void render(DrawContext ctx, int mx, int my) {
    int headerColor = isHovered(mx, my) ? 0xFF2A2A2A : 0xFF1A1A1A;
    ctx.fill((int)x, (int)y, (int)(x+width), (int)(y+height), headerColor);
    // Линия снизу заголовка для красоты
    ctx.fill((int)x, (int)(y+height-1), (int)(x+width), (int)(y+height), 0xFF00AAAA);
    
    ctx.drawText(ZeusClient.mc.textRenderer, category.displayName, (int)x + 5, (int)y + 5, 0xFFFFFFFF, true);

    if (!extended) return;
    double by = y + height;
    for (ModuleButton b : buttons) {
        b.x = x; b.y = by;
        b.render(ctx, mx, my);
        by += b.getHeight();
    }
    ctx.fill((int)x, (int)(y+height), (int)(x+width), (int)by, 0xAA000000);
}
