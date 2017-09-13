package colortales.colortalesapp;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by aroman on 03/09/2017.
 */
    public class consulta_comentario implements Serializable {

        @SerializedName("comentario")
        public String comentario;

        @SerializedName("calificacion")
        public float calificacion;
    }

