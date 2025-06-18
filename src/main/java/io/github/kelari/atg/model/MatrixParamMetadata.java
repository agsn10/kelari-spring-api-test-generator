package io.github.kelari.atg.model;

public class MatrixParamMetadata {

    private String paramName;     // Nome do parâmetro no método (ex: "cor")
    private String name;          // Valor de @MatrixVariable(name = "..."), se existir
    private String pathVar;       // Valor de @MatrixVariable(pathVar = "..."), se existir
    private String type;          // Tipo do parâmetro (ex: "String")

    public MatrixParamMetadata() {
    }

    public MatrixParamMetadata(String paramName, String type) {
        this.paramName = paramName;
        this.type = type;
    }

    public MatrixParamMetadata(String paramName, String name, String pathVar, String type) {
        this.paramName = paramName;
        this.name = name;
        this.pathVar = pathVar;
        this.type = type;
    }

    public String getParamName() {
        return paramName;
    }
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPathVar() {
        return pathVar;
    }
    public void setPathVar(String pathVar) {
        this.pathVar = pathVar;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
