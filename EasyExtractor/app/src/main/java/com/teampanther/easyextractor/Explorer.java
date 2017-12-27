package com.teampanther.easyextractor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Explorer {

    Context context;
    List pathFiles;
    String rootPath;

    public Explorer(Context context)
    {

        this.context = context;
    }

    public AdapterItems setItems(String path){

        File directorio = new File(path);
        File[] files = directorio.listFiles();
        pathFiles = new ArrayList();
        List pathFolders = new ArrayList();
        List nameFiles = new ArrayList();

       //Llamamos agregamos decisor para mostrar carpeta anterior

        /*IMPORTANTE LEER:
          en esta parte es donde tienes que implementar algun metodo para poder
          retroceder entre carpetas esta parte te la dejo a vos no es tan complicado
         */

        try {
            //Guardo Path de archivos a listar
            for (File archivo : files) {

                if (archivo.isFile() && !archivo.getName().equals(".android_secure")) {

                    pathFiles.add(archivo.getPath());

                } else if (!archivo.getName().equals(".android_secure")) {

                    pathFolders.add(archivo.getPath());
                }
            }

            if (files.length < 1) {

                Toast.makeText(context, "No existen Archivos para mostrar", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(context, "ERROR :" + e, Toast.LENGTH_LONG).show();
        }


        //ordeno paths en forma ascendente

        Collections.sort(pathFiles,String.CASE_INSENSITIVE_ORDER);
        Collections.sort(pathFolders,String.CASE_INSENSITIVE_ORDER);

        //ahora agrego paths de folders a path de archivos

        for (int i = 0;i < pathFiles.size();i++){

            pathFolders.add(pathFiles.get(i));
        }

        //lleno array adapter para mostrar en lista
        Items[] items = new Items[pathFolders.size()];

        for (int i = 0; i < pathFolders.size();i++){

            int image;

            File file = new File(pathFolders.get(i).toString());
            nameFiles.add(file.getName());

            if (file.isFile()){

                String name = file.getName(); //nombre del archivo
                int begin = name.length()-3; //Empiezo de substring
                int end = begin+3;
                //en caso que sea una imagen

                if ((name.substring(begin,end).toLowerCase().equals("jpg")) || (name.substring(begin,end).toLowerCase().equals("png"))){

                    image = R.drawable.ic_image_black_24dp;
                }else if (name.substring(begin,end).toLowerCase().equals("apk")){

                    image = R.drawable.ic_adb_black_24dp;
                }else{

                    image = R.drawable.ic_insert_drive_file_black_24dp;
                }

            }else {

                image = R.drawable.ic_folder_black_24dp;
            }

            items[i] = new Items(image,nameFiles.get(i).toString());
        }

        pathFiles = pathFolders;
        AdapterItems adapter = new AdapterItems(context,R.layout.explorer_layout,items);

        return adapter;
    }

    public List getPathFiles(){

        return pathFiles;
    }

    //Este es un procedimiento que lo que hace es mostrar al mantener presionado un item
    //distitas opciones como eliminar etc etc
    public void setDialog(File file,final String[] items){

        //seteo de imagen a mostrar para archivo
        int icon;
        if (file.isFile()){

            //Validacion de tipo de Archivo apk,jpg etc etc

            String name = file.getName(); //nombre del archivo
            int begin = name.length()-3; //Empiezo de substring
            int end = begin+3;
            //en caso que sea una imagen

            if ((name.substring(begin,end).equals("jpg")) || (name.substring(begin,end).equals("png"))){

                icon = R.drawable.ic_image_black_24dp;
            }else if (name.substring(begin,end).equals("apk")){

                icon = R.drawable.ic_adb_black_24dp;
            }else{

                icon = R.drawable.ic_insert_drive_file_black_24dp;
            }

        }else{

            icon = R.drawable.ic_folder_black_24dp;
        }

        //En esta parte de aqui es donde se valida el evento elegido podrias implementar un switch
        //y mandar a llamar metodos para ciertas opciones
        String fileName = file.getName();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(fileName);
        builder.setIcon(icon);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(context,"Accion: "+items[which],Toast.LENGTH_LONG).show();

            }
        });

        builder.create().show();
    }

}