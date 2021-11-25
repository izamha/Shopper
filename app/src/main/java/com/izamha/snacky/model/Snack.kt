package com.izamha.snacky.model

import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "snack_table")
data class Snack(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String? = null,

    @ColumnInfo(name = "price")
    var price: Long? = null,

    var tagline: String = "",
) : Parcelable {
    constructor(
        id: Long,
        name: String,
        imageUrl: String,
        price: Long
    ) : this(id) {
        this.id = id
        this.name = name
        this.imageUrl = imageUrl
        this.price = price
    }
}

/**
 * Gone is the static data. Let's unleash the Firebase
 */
val database = Firebase.database
val userId = FirebaseAuth.getInstance().currentUser?.uid
val myRef = database.reference.child("Snacks").child(userId!!)
val onlineSnacksList = mutableListOf<Snack>()

fun fromFirebase(): MutableList<Snack> {
    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                for (ds in snapshot.children) {
                    val data = ds.getValue(Snack::class.java)
                    onlineSnacksList.add(data!!)
                }
            }
        }
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })
    return onlineSnacksList
}

val data = fromFirebase() as List<Snack>

/**
 * Static data
 */

val snacks = listOf(
    Snack(
        id = 1L,
        name = "Inshyushyu",
        tagline = "A tag line",
        // imageUrl = "https://source.unsplash.com/pGM4sjt_BdQ",
        imageUrl = "https://images.unsplash.com/photo-1550583724-b2692b85b150?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=387&q=80",
        price = 500
    ),
    Snack(
        id = 2L,
        name = "Cakes",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/Yc5sL-ejk6U",
        price = 150
    ),
    Snack(
        id = 3L,
        name = "Nuts(Ubunyobwa)",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/-LojFX9NfPY",
        price = 200
    ),
    Snack(
        id = 4L,
        name = "Cookies",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/3U2V5WqK1PQ",
        price = 100
    ),
    Snack(
        id = 5L,
        name = "Gingerbread",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/Y4YR9OjdIMk",
        price = 500
    ),
    Snack(
        id = 6L,
        name = "Honeycomb",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/bELvIg_KZGU",
        price = 350
    ),
    Snack(
        id = 7L,
        name = "Ice Cream Sandwich",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/YgYJsFDd4AU",
        price = 1300
    ),
    Snack(
        id = 8L,
        name = "Jellybean",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/0u_vbeOkMpk",
        price = 500
    ),
    Snack(
        id = 9L,
        name = "KitKat",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/yb16pT5F_jE",
        price = 550
    ),
    Snack(
        id = 10L,
        name = "Lollipop",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/AHF_ZktTL6Q",
        price = 100
    ),
    Snack(
        id = 11L,
        name = "Marshmallow",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/rqFm0IgMVYY",
        price = 230
    ),
    Snack(
        id = 12L,
        name = "Nougat",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/qRE_OpbVPR8",
        price = 400
    ),
    Snack(
        id = 13L,
        name = "Oreo",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/33fWPnyN6tU",
        price = 300
    ),
    Snack(
        id = 14L,
        name = "Pie",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/aX_ljOOyWJY",
        price = 200
    ),
    Snack(
        id = 15L,
        name = "Chips",
        imageUrl = "https://source.unsplash.com/UsSdMZ78Q3E",
        price = 300
    ),
    Snack(
        id = 16L,
        name = "Pretzels",
        imageUrl = "https://source.unsplash.com/7meCnGCJ5Ms",
        price = 400
    ),
    Snack(
        id = 17L,
        name = "Smoothies",
        imageUrl = "https://source.unsplash.com/m741tj4Cz7M",
        price = 280
    ),
    Snack(
        id = 18L,
        name = "Popcorn",
        imageUrl = "https://source.unsplash.com/iuwMdNq0-s4",
        price = 100
    ),
    Snack(
        id = 19L,
        name = "Almonds",
        imageUrl = "https://source.unsplash.com/qgWWQU1SzqM",
        price = 250
    ),
    Snack(
        id = 20L,
        name = "Cheese",
        imageUrl = "https://source.unsplash.com/9MzCd76xLGk",
        price = 290
    ),
    Snack(
        id = 21L,
        name = "Apples",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/1d9xXWMtQzQ",
        price = 250
    ),
    Snack(
        id = 22L,
        name = "Apple sauce",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/wZxpOw84QTU",
        price = 290
    ),
    Snack(
        id = 23L,
        name = "Apple chips",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/okzeRxm_GPo",
        price = 390
    ),
    Snack(
        id = 24L,
        name = "Apple juice",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/l7imGdupuhU",
        price = 500
    ),
    Snack(
        id = 25L,
        name = "Apple pie",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/bkXzABDt08Q",
        price = 290
    ),
    Snack(
        id = 26L,
        name = "Grapes",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/y2MeW00BdBo",
        price = 400
    ),
    Snack(
        id = 27L,
        name = "Kiwi",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/1oMGgHn-M8k",
        price = 400
    ),
    Snack(
        id = 28L,
        name = "Mango",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/TIGDsyy0TK4",
        price = 300
    )
)
