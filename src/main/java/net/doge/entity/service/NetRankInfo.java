package net.doge.entity.service;

import lombok.Data;
import net.doge.constant.service.source.NetResourceSource;
import net.doge.entity.service.base.NetResource;
import net.doge.util.core.StringUtil;

import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;
import java.util.Objects;

import net.doge.util.core.img.ImageCache;

/**
 * @author Doge
 * @description 榜单
 * @date 2020/12/7
 */
@Data
public class NetRankInfo implements NetResource {
    // 榜单来源
    private int source = NetResourceSource.NC;
    // 榜单 id
    private String id;
    // 榜单名称
    private String name;
    // 封面图片
    private SoftReference<BufferedImage> coverImgRef;
    // 封面图片 url
    private String coverImgUrl;
    // 封面图片缩略图
    private SoftReference<BufferedImage> coverImgThumbRef;
    // 描述
    private String description;
    // 播放量
    private Long playCount;
    // 更新频率
    private String updateFre;
    // 更新时间
    private String updateTime;

    // 缩略图加载后的回调函数
    private Runnable invokeLater;

    public boolean hasName() {
        return StringUtil.notEmpty(name);
    }

    public boolean hasPlayCount() {
        return playCount != null && playCount >= 0;
    }

    public boolean hasUpdateFre() {
        return StringUtil.notEmpty(updateFre);
    }

    public boolean hasUpdateTime() {
        return StringUtil.notEmpty(updateTime);
    }

    public boolean hasCoverImgUrl() {
        return StringUtil.notEmpty(coverImgUrl);
    }

    public boolean hasCoverImg() {
        if (StringUtil.notEmpty(coverImgUrl)) return ImageCache.get(coverImgUrl) != null;
        return coverImgRef != null && coverImgRef.get() != null;
    }
    
    public BufferedImage getCoverImg() {
        if (StringUtil.notEmpty(coverImgUrl)) return ImageCache.get(coverImgUrl);
        return coverImgRef != null ? coverImgRef.get() : null;
    }

    public void setCoverImgThumb(BufferedImage coverImgThumb) {
        if (StringUtil.notEmpty(coverImgUrl)) {
            ImageCache.put(coverImgUrl + "_thumb", coverImgThumb);
        } else {
            this.coverImgThumbRef = new SoftReference<>(coverImgThumb);
        }
        callback();
    }
    
    public BufferedImage getCoverImgThumb() {
        if (StringUtil.notEmpty(coverImgUrl)) return ImageCache.get(coverImgUrl + "_thumb");
        return coverImgThumbRef != null ? coverImgThumbRef.get() : null;
    }

    public void setCoverImg(BufferedImage coverImg) {
        if (StringUtil.notEmpty(coverImgUrl)) {
            ImageCache.put(coverImgUrl, coverImg);
        } else {
            this.coverImgRef = new SoftReference<>(coverImg);
        }
        callback();
    }

    private void callback() {
        if (invokeLater == null) return;
        invokeLater.run();
        // 调用后丢弃
        invokeLater = null;
    }

    /**
     * 判断榜单信息是否完整
     *
     * @return
     */
    public boolean isIntegrated() {
        return hasCoverImg();
    }

    public boolean hasCoverImgThumb() {
        if (StringUtil.notEmpty(coverImgUrl)) return ImageCache.get(coverImgUrl + "_thumb") != null;
        return coverImgThumbRef != null && coverImgThumbRef.get() != null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NetRankInfo) {
            NetRankInfo rankInfo = (NetRankInfo) o;
            return hashCode() == rankInfo.hashCode();
        }
        return false;
    }

    // 必须重写 hashCode 和 equals 方法才能在 Set 判断是否重复！
    @Override
    public int hashCode() {
        return Objects.hash(source, id);
    }

    public String toString() {
        return NetResourceSource.NAMES[source] + " - " + toSimpleString();
    }

    public String toSimpleString() {
        return name;
    }
}
