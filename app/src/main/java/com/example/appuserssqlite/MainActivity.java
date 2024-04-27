package com.example.appuserssqlite;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appuserssqlite.R;

public class MainActivity extends AppCompatActivity {

    TextView message, buscarTitulo, identificadorLibro, costoLibro, nameAutor;
    Button search, delete, save, edit;
    dblibrary objBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message = findViewById(R.id.tvMessage);
        buscarTitulo = findViewById(R.id.etBuscarTitulo);
        identificadorLibro = findViewById(R.id.etIdentificadorLibro);
        costoLibro = findViewById(R.id.etCostoLibro);
        search = findViewById(R.id.btSearch);
        delete = findViewById(R.id.btdelete);
        save = findViewById(R.id.btSave);
        edit = findViewById(R.id.btEditar);
        nameAutor = findViewById(R.id.etnombreAutor);

        objBook = new dblibrary(this, "dbBook", null, 1);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBookById();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBook();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook();
            }
        });
    }

    private void searchBookById() {
        String mIdLibro = identificadorLibro.getText().toString(); // Usar identificadorLibro para ingresar el ID a buscar

        if (!mIdLibro.isEmpty()) {
            SQLiteDatabase sdbBook = objBook.getReadableDatabase();
            try {
                Cursor cBook = sdbBook.rawQuery("SELECT namebook, costebook, nameautor FROM book WHERE idbook = ?", new String[]{mIdLibro});

                if (cBook.moveToFirst()) {
                    buscarTitulo.setText(cBook.getString(0)); // Mostrar el título del libro
                    costoLibro.setText(cBook.getString(1)); // Mostrar el costo del libro
                    nameAutor.setText(cBook.getString(2)); // Mostrar el nombre del autor
                    message.setText("");
                    message.setTextColor(Color.GREEN);
                } else {
                    message.setTextColor(Color.RED);
                    message.setText("El libro con el ID especificado no está en la base de datos.");
                }

                cBook.close();
            } catch (Exception e) {
                e.printStackTrace();
                message.setTextColor(Color.RED);
                message.setText("Error al buscar el libro.");
            } finally {
                sdbBook.close();
            }
        } else {
            message.setTextColor(Color.RED);
            message.setText("Debe ingresar el ID de un libro.");
        }
    }


    private void addBook() {
        String mTituloBuscado = buscarTitulo.getText().toString();
        String mNombreAutor = nameAutor.getText().toString();
        String mCostBook = costoLibro.getText().toString();
        String mIdentificadorLibroTexto = identificadorLibro.getText().toString();

        if (!mIdentificadorLibroTexto.isEmpty() && !mTituloBuscado.isEmpty() && !mNombreAutor.isEmpty() && !mCostBook.isEmpty()) {
            try {
                Integer mIdentificadorLibro = Integer.parseInt(mIdentificadorLibroTexto);
                if (!searchBook(mIdentificadorLibro)) {
                    SQLiteDatabase odbBookW = objBook.getWritableDatabase();
                    ContentValues cvBook = new ContentValues();
                    cvBook.put("idbook", mIdentificadorLibro);
                    cvBook.put("namebook", mTituloBuscado);
                    cvBook.put("costebook", mCostBook);
                    cvBook.put("nameautor", mNombreAutor);
                    odbBookW.insert("book", null, cvBook);
                    odbBookW.close();
                    message.setTextColor(Color.GREEN);
                    message.setText("Libro agregado correctamente");
                } else {
                    message.setTextColor(Color.RED);
                    message.setText("El ID del libro ya existe en la base de datos");
                }
            } catch (NumberFormatException e) {
                message.setTextColor(Color.RED);
                message.setText("El ID del libro debe ser un número entero válido");
            }
        } else {
            message.setTextColor(Color.RED);
            message.setText("Debe ingresar todos los datos requeridos");
        }
    }

    private void updateBook() {
        String mIdLibro = identificadorLibro.getText().toString();
        String mTituloLibro = buscarTitulo.getText().toString();

        if (!mIdLibro.isEmpty()) {
            SQLiteDatabase sdbBookW = objBook.getWritableDatabase();
            try {
                sdbBookW.execSQL("UPDATE book SET namebook = ?, costebook = ?, nameautor = ? WHERE idbook = ?", new String[]{mTituloLibro, costoLibro.getText().toString(), nameAutor.getText().toString(), mIdLibro});
                message.setTextColor(Color.GREEN);
                message.setText("Libro actualizado correctamente");
            } catch (Exception e) {
                e.printStackTrace();
                message.setTextColor(Color.RED);
                message.setText("Error al actualizar el libro");
            } finally {
                sdbBookW.close();
            }
        } else {
            message.setTextColor(Color.RED);
            message.setText("Debe buscar un libro antes de editar");
        }
    }

    private void deleteBook() {
        String mIdLibro = identificadorLibro.getText().toString();

        if (!mIdLibro.isEmpty()) {
            SQLiteDatabase odbBook = objBook.getWritableDatabase();
            try {
                int rowsDeleted = odbBook.delete("book", "idbook = ?", new String[]{mIdLibro});

                if (rowsDeleted > 0) {
                    message.setTextColor(Color.GREEN);
                    message.setText("El libro ha sido eliminado correctamente");
                } else {
                    message.setTextColor(Color.RED);
                    message.setText("No se encontró el libro con el ID especificado");
                }
            } catch (Exception e) {
                e.printStackTrace();
                message.setTextColor(Color.RED);
                message.setText("Error al eliminar el libro");
            } finally {
                odbBook.close();
            }
        } else {
            message.setTextColor(Color.RED);
            message.setText("Debe buscar un libro antes de eliminar");
        }
    }

    private boolean searchBook(Integer mIdLibro) {
        SQLiteDatabase sdbBook = objBook.getReadableDatabase();
        Cursor cBook = sdbBook.rawQuery("SELECT idbook FROM book WHERE idbook = ?", new String[]{String.valueOf(mIdLibro)});
        boolean exists = cBook.moveToFirst();
        cBook.close();
        sdbBook.close();
        return exists;
    }
}

class dblibrary extends SQLiteOpenHelper {
    public dblibrary(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE book (idbook INTEGER PRIMARY KEY, namebook TEXT, costebook TEXT, nameautor TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS book");
        onCreate(db);
    }
}