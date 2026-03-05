package net.doge.util.core.img;

import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 全局图片缓存，使用 LRU 算法防止内存溢出
 */
public class ImageCache {
    // 最大缓存数量，根据 64G 内存占用 6G 的情况，大约 200-300 张图片（假设每张 1-5MB）可能占用 1GB 左右。
    // 为了安全起见，限制为 100 张。列表渲染通常只需要几十张。
    private static final int MAX_ENTRIES = 100;

    private static final Map<String, BufferedImage> cache = new LinkedHashMap<String, BufferedImage>(MAX_ENTRIES + 1, 0.75F, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, BufferedImage> eldest) {
            return size() > MAX_ENTRIES;
        }
    };

    public static synchronized BufferedImage get(String key) {
        if (key == null) return null;
        return cache.get(key);
    }

    public static synchronized void put(String key, BufferedImage image) {
        if (key == null || image == null) return;
        cache.put(key, image);
    }

    public static synchronized boolean contains(String key) {
        return cache.containsKey(key);
    }
    
    public static synchronized void clear() {
        cache.clear();
    }
}
