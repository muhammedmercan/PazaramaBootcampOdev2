package com.example.pazaramabootcampodev2.data.repository

import android.content.ContentValues
import android.content.Context
import com.example.pazaramabootcampodev2.data.local.DBGateway
import com.example.pazaramabootcampodev2.domain.model.Kategori

class KategoriRepository(c: Context)
{
    private var context: Context? = null
    private var dbg: DBGateway? = null

    init {
        context = c
        dbg = DBGateway(c)
    }

    public fun KategorileriOlustur()
    {
        var db = dbg!!.writableDatabase

        var cur = db.rawQuery("Select * from Kategoriler", null)

        if (cur.count == 0) {
            var cv = ContentValues()
            cv.put("Aciklama", "Motor")
            db.insert("Kategoriler", null, cv)

            cv = ContentValues()
            cv.put("Aciklama", "Kaporta")
            db.insert("Kategoriler", null, cv)

            cv = ContentValues()
            cv.put("Aciklama", "Elektrik")
            db.insert("Kategoriler", null, cv)

            cv = ContentValues()
            cv.put("Aciklama", "Aksesuar")
            db.insert("Kategoriler", null, cv)
        }

        db.close()
    }

    public fun GetKategoriler() : MutableList<Kategori>
    {
        var db = dbg!!.readableDatabase

        var cur = db.rawQuery("Select * from Kategoriler", null)

        var lst = mutableListOf<Kategori>()

        while (cur.moveToNext())
        {
            lst.add(
                Kategori(
                    cur.getInt(0),
                    cur.getString(1))
            )
        }

        return lst
    }
}