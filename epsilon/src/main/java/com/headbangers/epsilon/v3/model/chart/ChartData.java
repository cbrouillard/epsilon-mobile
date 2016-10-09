package com.headbangers.epsilon.v3.model.chart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties({"class"})
public class ChartData {

    @JsonProperty("colors")
    private List<String> colors;

    @JsonProperty("graphData")
    private List<GraphData> data;

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public List<GraphData> getData() {
        return data;
    }

    public void setData(List<GraphData> data) {
        this.data = data;
    }
}

