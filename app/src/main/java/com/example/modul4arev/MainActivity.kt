package com.example.modul4arev

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    TicketBookingScreen()
                }
            }
        }
    }
}

// Enum untuk merepresentasikan status pemesanan
enum class BookingStatus {
    IDLE, ERROR_EMPTY_NAME, PROCESSING, SUCCESS
}

// 1. PARENT COMPOSABLE (Tempat State Dikelola / State Hoisting)
@Composable
fun TicketBookingScreen() {
    // State menggunakan rememberSaveable agar data tidak hilang saat rotasi layar
    var buyerName by rememberSaveable { mutableStateOf("") }
    var ticketCount by rememberSaveable { mutableStateOf(1) }
    var ticketPrice by rememberSaveable { mutableIntStateOf(50000) } // Harga Tiket (State)
    var bookingStatus by rememberSaveable { mutableStateOf(BookingStatus.IDLE) }

    // Efek Samping: Menjalankan delay 5 detik saat status berubah menjadi PROCESSING
    LaunchedEffect(bookingStatus) {
        if (bookingStatus == BookingStatus.PROCESSING) {
            delay(5000L) // Memproses pesanan selama 5 detik sesuai instruksi
            bookingStatus = BookingStatus.SUCCESS
        }
    }

    // Memanggil Child Composable dan meneruskan State serta Event (State Hoisting)
    TicketBookingContent(
        name = buyerName,
        onNameChange = {
            buyerName = it
            // Reset pesan error jika user mulai mengetik ulang
            if (bookingStatus == BookingStatus.ERROR_EMPTY_NAME) {
                bookingStatus = BookingStatus.IDLE
            }
        },
        ticketCount = ticketCount,
        ticketPrice = ticketPrice,
        onIncrement = { ticketCount++ },
        onDecrement = { if (ticketCount > 1) ticketCount-- },
        status = bookingStatus,
        onBookClick = {
            if (buyerName.isBlank()) {
                bookingStatus = BookingStatus.ERROR_EMPTY_NAME
            } else {
                bookingStatus = BookingStatus.PROCESSING
            }
        }
    )
}

// 2. CHILD COMPOSABLE (Hanya Menerima Data dan Meneruskan Event)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketBookingContent(
    name: String,
    onNameChange: (String) -> Unit,
    ticketCount: Int,
    ticketPrice: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    status: BookingStatus,
    onBookClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pemesanan Tiket", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF195BB2))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Field Nama
            Text("Nama", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = { Text("Masukkan nama Anda") },
                modifier = Modifier.fillMaxWidth(),
                enabled = status != BookingStatus.PROCESSING // Disable saat memproses
            )

            // Field Jumlah Tiket
            Text("Jumlah Tiket (Harga: Rp${ticketPrice}/tiket)", fontWeight = FontWeight.Bold)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tombol Minus
                Button(
                    onClick = onDecrement,
                    enabled = status != BookingStatus.PROCESSING && ticketCount > 1,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5E7EB), contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                // Angka Jumlah Tiket
                Text(text = ticketCount.toString(), fontSize = 18.sp, fontWeight = FontWeight.Bold)

                // Tombol Plus
                Button(
                    onClick = onIncrement,
                    enabled = status != BookingStatus.PROCESSING,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5E7EB), contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tombol Pesan Tiket
            Button(
                onClick = onBookClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF195BB2),
                    disabledContainerColor = Color(0xFFA1B3CC)
                ),
                enabled = status != BookingStatus.PROCESSING
            ) {
                Text(
                    text = if (status == BookingStatus.PROCESSING) "Memproses..." else "Pesan Tiket",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            // Box Status Pemberitahuan
            StatusBox(status = status)
        }
    }
}

// Composable terpisah untuk Box Status bagian bawah
@Composable
fun StatusBox(status: BookingStatus) {
    val (backgroundColor, textColor, icon, message) = when (status) {
        BookingStatus.IDLE -> listOf(
            Color(0xFFF3F4F6), Color.Black, null, "Status: Silakan pesan tiket"
        )
        BookingStatus.ERROR_EMPTY_NAME -> listOf(
            Color(0xFFFFEBEB), Color(0xFFD32F2F), Icons.Default.Warning, "Status : Nama Masih Kosong"
        )
        BookingStatus.PROCESSING -> listOf(
            Color(0xFFE3F2FD), Color(0xFF1976D2), null, "Status : Memproses pesanan........."
        )
        BookingStatus.SUCCESS -> listOf(
            Color(0xFFE8F5E9), Color(0xFF2E7D32), Icons.Default.CheckCircle, "Status : Tiket telah dipesan"
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor as Color, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (status == BookingStatus.PROCESSING) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = textColor as Color,
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
        } else if (icon != null) {
            Icon(
                imageVector = icon as androidx.compose.ui.graphics.vector.ImageVector,
                contentDescription = "Status Icon",
                tint = textColor as Color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = message as String,
            color = textColor as Color,
            fontWeight = FontWeight.Medium
        )
    }
}