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
 * @description 专辑
 * @date 2020/12/7
 */
@Data
public class NetAlbumInfo implements NetResource {
    // 专辑来源
    private int source = NetResourceSource.NC;
    // 专辑 id
    private String id;
    // 专辑名称
    private String name;
    // 艺术家
    private String artist;
    // 艺术家 id
    private String artistId;
    // 封面图片
    private SoftReference<BufferedImage> coverImgRef;
    // 封面图片 url
    private String coverImgUrl;
    // 封面图片缩略图
    private SoftReference<BufferedImage> coverImgThumbRef;
    // 封面图片缩略图 url
    private String coverImgThumbUrl;
    // 描述
    private String description;
    // 发行日期
    private String publishTime;
    // 歌曲数量
    private Integer songNum;
    
    // 缩略图加载后的回调函数
    private Runnable invokeLater;

    public boolean isPhoto() {
        return source == NetResourceSource.DT;
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

    public boolean hasName() {
        return StringUtil.notEmpty(name);
    }

    public boolean hasArtist() {
        return StringUtil.notEmpty(artist);
    }

    public boolean hasArtistId() {
        return StringUtil.notEmpty(artistId);
    }

    public boolean hasPublishTime() {
        return StringUtil.notEmpty(publishTime);
    }

    public boolean hasSongNum() {
        return songNum != null && songNum >= 0;
    }

    public void setCoverImgThumb(BufferedImage coverImgThumb) {
        if (StringUtil.notEmpty(coverImgThumbUrl)) {
            ImageCache.put(coverImgThumbUrl, coverImgThumb);
        } else {
            this.coverImgThumbRef = new SoftReference<>(coverImgThumb);
        }
        callback();
    }
    
    public BufferedImage getCoverImgThumb() {
        if (StringUtil.notEmpty(coverImgThumbUrl)) return ImageCache.get(coverImgThumbUrl);
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
     * 判断专辑信息是否完整
     *
     * @return
     */
    public boolean isIntegrated() {
        return hasCoverImg();
    }

    public boolean hasCoverImgThumb() {
        if (StringUtil.notEmpty(coverImgThumbUrl)) return ImageCache.get(coverImgThumbUrl) != null;
        return coverImgThumbRef != null && coverImgThumbRef.get() != null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NetAlbumInfo) {
            NetAlbumInfo albumInfo = (NetAlbumInfo) o;
            return hashCode() == albumInfo.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, id);
    }

    public String toString() {
        return NetResourceSource.NAMES[source] + " - " + toSimpleString();
    }

    public String toSimpleString() {
        return name
                + (StringUtil.isEmpty(artist) ? "" : " - " + artist);
    }
}
