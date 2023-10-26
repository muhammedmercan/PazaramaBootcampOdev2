package com.example.pazaramabootcampodev2.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pazaramabootcampodev2.data.repository.KategoriRepository
import com.example.pazaramabootcampodev2.data.repository.ParcaRepository
import com.example.pazaramabootcampodev2.domain.model.Parca
import com.example.pazaramabootcampodev2.ui.theme.PazaramaBootcampOdev2Theme


class MainActivity : ComponentActivity() {
    private lateinit var kategoriRepository: KategoriRepository
    private lateinit var parcaRepository: ParcaRepository
    private lateinit var kategorikParcalar:MutableList<Parca>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        kategoriRepository = KategoriRepository(this)
        parcaRepository = ParcaRepository(this)

        kategoriRepository.KategorileriOlustur()

        setContent {

            PazaramaBootcampOdev2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {

                    val navController = rememberNavController()


                    NavHost(navController = navController, startDestination = "mainScreen") {
                        composable("mainScreen") { MainScreen(navController) }
                        composable("addScreen") { AddScreen(navController) }
                    }

                    kategorikParcalar = parcaRepository.ParcalarByKategoriID(1)

                }
            }
        }
    }

    @Composable
    fun MainScreen(navController: NavController)
    {
        val kategoriler = kategoriRepository.GetKategoriler()

        var expanded = remember { mutableStateOf(false) }
        var secilenKategori = remember { mutableStateOf(kategoriler[0] )  }
        kategorikParcalar = parcaRepository.ParcalarByKategoriID(secilenKategori.value.K_ID)


        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Cyan),
            Arrangement.Top, Alignment.Start
        )
        {
            Box(modifier = Modifier
                .fillMaxWidth()
                .background(Color.Magenta)
                .height(35.dp))
            {
                Row(
                    Modifier
                        .clickable {
                            expanded.value = !expanded.value
                        }
                        .align(Alignment.TopStart))
                {
                    Text(text = secilenKategori.value.Aciklama)
                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(expanded = expanded.value,
                    onDismissRequest = {  expanded.value = false })
                {
                    kategoriler.forEach {

                        DropdownMenuItem(
                            text =
                            {
                                Text(text = it.Aciklama )
                            },
                            onClick = {
                                secilenKategori.value = it

                                expanded.value = false

                                kategorikParcalar = parcaRepository.ParcalarByKategoriID(secilenKategori.value.K_ID)
                            })
                    }
                }
            }

            ParcaListesiGoster(lst = kategorikParcalar)
            FloatingActionButton(
                onClick = {  navController.navigate("addScreen")},
                modifier = Modifier.align(Alignment.End),
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddScreen(navController: NavController) {

        val kategoriler = kategoriRepository.GetKategoriler()

        var expanded = remember { mutableStateOf(false) }
        var secilenKategori = remember { mutableStateOf(kategoriler[0] )  }

        // Ürün eklemek için kullanılan sayfa
        val name = remember { mutableStateOf("") }
        val stock = remember { mutableStateOf(0) }
        val price = remember { mutableStateOf(0L) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Yeni Parca Ekle", fontSize = 24.sp)

            // Ürün bilgileri için giriş alanları
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Parca Adı") }
            )

            OutlinedTextField(
                value = stock.value.toString(),
                onValueChange = { stock.value = it.toIntOrNull() ?: 0 },
                label = { Text("Stok Adedi") }
            )

            OutlinedTextField(
                value = price.value.toString(),
                onValueChange = { price.value = it.toLongOrNull() ?: 0L },
                label = { Text("Fiyatı") }
            )

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(35.dp))
            {
                Row(
                    Modifier
                        .clickable {
                            expanded.value = !expanded.value
                        }
                        .align(Alignment.TopStart))
                {
                    Text(text = secilenKategori.value.Aciklama)
                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(expanded = expanded.value,
                    onDismissRequest = {  expanded.value = false })
                {
                    kategoriler.forEach {

                        DropdownMenuItem(
                            text =
                            {
                                Text(text = it.Aciklama )
                            },
                            onClick = {
                                secilenKategori.value = it

                                expanded.value = false

                                kategorikParcalar = parcaRepository.ParcalarByKategoriID(secilenKategori.value.K_ID)
                            })
                    }
                }
            }

            Button(
                onClick = {

                    val parca = Parca(
                        Adi = name.value,
                        StokAdedi = stock.value,
                        Fiyati = price.value,
                        Kategori_ID =  secilenKategori.value.K_ID// Kategori ID'sini ayarlayın
                    )
                    parcaRepository.ParcaEkle(parca)
                    navController.navigate("mainScreen")
                    //navController.popBackStack()


                }
            ) {
                Text("Kaydet")
            }
        }
    }

    @Composable
    fun ParcaGoster(p:Parca)
    {
        var ctx = LocalContext.current

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White))
        {
            Text(text = p.Adi, style = MaterialTheme.typography.headlineMedium)
            Spacer(
                Modifier
                    .padding(1.dp)
                    .fillMaxWidth(0.7F)
                    .background(Color.DarkGray)
                    .height(0.5.dp))
            Text(text = "Stok Adedi: ${p.StokAdedi}", style = MaterialTheme.typography.bodyMedium)
            Spacer(
                Modifier
                    .padding(1.dp)
                    .fillMaxWidth(0.7F)
                    .background(Color.DarkGray)
                    .height(0.5.dp))
            Text(text = "Fiyatı: ${p.Fiyati} TL", style = MaterialTheme.typography.bodyMedium)
            Spacer(
                Modifier
                    .padding(1.dp)
                    .fillMaxWidth(0.7F)
                    .background(Color.Red)
                    .height(1.5.dp))
        }
    }

    @Composable
    fun ParcaListesiGoster(lst:List<Parca>)
    {
        LazyColumn(Modifier.border(3.dp, Color.Blue),
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.Bottom,
            userScrollEnabled = true
        )
        {
            this.items(lst)
            {
                ParcaGoster(p = it)
            }
        }
    }
}



