package com.github.nnkwrik.doutuBot.model;

import lombok.Data;
import java.util.List;

@Data
public class DoutulaData {
    private List<EmoInfo> list;
    private Integer more;
}
