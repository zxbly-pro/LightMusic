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
 * @description 歌单
 * @date 2020/12/7
 */
@Data
public class NetPlaylistInfo implements NetResource {
    // 歌单来源
    private int source = NetResourceSource.NC;
    // 歌单 id
    private String id;
    // 歌单名称
    private String name;
    // 创建者
    private String creator;
    // 创建者 id
    private String creatorId;
    // 封面图片
    private SoftReference<BufferedImage> coverImgRef;
    // 封面图片缩略图
    private SoftReference<BufferedImage> coverImgThumbRef;
    // 封面图片 url
    private String coverImgUrl;
    // 封面图片缩略图 url
    private String coverImgThumbUrl;
    // 描述
    private String description;
    // 更新时间
//    private String updateTime;
    // 标签
    private String tag;
    // 播放量
    private Long playCount;
    // 歌曲数量
    private Integer trackCount;
    
    // 缩略图加载后的回调函数
    private Runnable invokeLater;

    public boolean hasName() {
        return StringUtil.notEmpty(name);
    }

    public boolean hasCreator() {
        return StringUtil.notEmpty(creator);
    }

    public boolean hasCreatorId() {
        return StringUtil.notEmpty(creatorId);
    }

    public boolean hasTag() {
        return StringUtil.notEmpty(tag);
    }

    public boolean hasTrackCount() {
        return trackCount != null && trackCount >= 0;
    }

    public boolean hasPlayCount() {
        return playCount != null && playCount >= 0;
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

    public boolean hasCoverImgThumb() {
        if (StringUtil.notEmpty(coverImgThumbUrl)) return ImageCache.get(coverImgThumbUrl) != null;
        return coverImgThumbRef != null && coverImgThumbRef.get() != null;
    }
    
    public BufferedImage getCoverImgThumb() {
        if (StringUtil.notEmpty(coverImgThumbUrl)) return ImageCache.get(coverImgThumbUrl);
        return coverImgThumbRef != null ? coverImgThumbRef.get() : null;
    }

    public void setCoverImgThumb(BufferedImage coverImgThumb) {
        if (StringUtil.notEmpty(coverImgThumbUrl)) {
            ImageCache.put(coverImgThumbUrl, coverImgThumb);
        } else {
            this.coverImgThumbRef = new SoftReference<>(coverImgThumb);
        }
        callback();
    }

    public void setCoverImg(BufferedImage coverImg) {
        if (StringUtil.notEmpty(coverImgUrl)) {
            ImageCache.put(coverImgUrl, coverImg);
        } else {
            this.coverImgRef = new SoftReference<>(coverImg);
        }
        callback();
    }

    public void callback() {
        if (invokeLater == null) return;
        invokeLater.run();
        // 调用后丢弃
        invokeLater = null;
    }

//    private String buildCoverImgThumbPath() {
//        return SimplePath.IMG_CACHE_PATH + source + " - " + id + " - playlist - thumb.png";
//    }
//
//    private String buildCoverImgPath() {
//        return SimplePath.IMG_CACHE_PATH + source + " - " + id + " - playlist.png";
//    }

    /**
     * 判断歌单信息是否完整
     *
     * @return
     */
    public boolean isIntegrated() {
        return hasCoverImg();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof NetPlaylistInfo) {
            NetPlaylistInfo playlistInfo = (NetPlaylistInfo) o;
            return hashCode() == playlistInfo.hashCode();
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
                + (StringUtil.isEmpty(creator) ? "" : " - " + creator);
    }
}
