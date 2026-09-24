package com.shoplite.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.shoplite.app.utils.fmt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpen: (Int) -> Unit,
    onCart: () -> Unit,
    onLogout: () -> Unit,
    vm: HomeViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsState()
    var showProfile by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.callApi()
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3949AB))
                    .padding(top = 36.dp, start = 16.dp, end = 8.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.userImage != null) {
                    AsyncImage(
                        model = state.userImage,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { showProfile = true }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF5C6BC0))
                            .clickable { showProfile = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Hi, " + (state.userName ?: "there") + " 👋",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "What are you shopping for today?",
                        color = Color(0xFFC5CAE9),
                        fontSize = 12.sp
                    )
                }
                IconButton(onClick = { vm.doIt() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                }
                IconButton(onClick = onCart) {
                    BadgedBox(
                        badge = {
                            if (state.cartCount > 0) {
                                Badge { Text(state.cartCount.toString()) }
                            }
                        }
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.White)
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {
            OutlinedTextField(
                value = state.query,
                onValueChange = { vm.handle(it) },
                placeholder = { Text("Search products") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )

            LazyRow(
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = state.selectedCategory == null,
                        onClick = { vm.select(null) },
                        label = { Text("All") }
                    )
                }
                itemsIndexed(state.categories) { _, c ->
                    FilterChip(
                        selected = state.selectedCategory == c,
                        onClick = { vm.select(c) },
                        label = {
                            Text(c.replace("-", " ").replaceFirstChar { it.uppercase() })
                        }
                    )
                }
            }

            if (state.error != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFEBEE))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.error ?: "",
                        color = Color(0xFFC62828),
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { vm.doIt() }) { Text("Retry") }
                }
            }

            if (state.loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.products.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🛍️", fontSize = 48.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("No products found", fontWeight = FontWeight.SemiBold)
                        if (state.query.isNotEmpty()) {
                            Text(
                                "Try a different search",
                                color = Color.Gray,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(state.products) { index, p ->
                        if (index == state.products.size - 1) {
                            vm.more()
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clickable { onOpen(p.id) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Box {
                                    AsyncImage(
                                        model = p.thumbnail,
                                        contentDescription = p.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(96.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFEEEEEE))
                                    )
                                    if (p.discountPercentage > 10) {
                                        Text(
                                            text = "-" + p.discountPercentage.toInt() + "%",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFFE53935))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = p.title,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (p.brand != null) {
                                        Text(text = p.brand, color = Color.Gray, fontSize = 12.sp)
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        for (i in 1..5) {
                                            Icon(
                                                Icons.Default.Star,
                                                contentDescription = null,
                                                tint = if (i <= p.rating.toInt()) Color(0xFFFFB300) else Color(0xFFE0E0E0),
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            text = String.format("%.1f", p.rating),
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    Spacer(Modifier.height(6.dp))
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        val discounted = p.price * (1 - p.discountPercentage / 100)
                                        Text(
                                            text = fmt(discounted),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp,
                                            color = Color(0xFF1B5E20)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        if (p.discountPercentage > 0) {
                                            Text(
                                                text = fmt(p.price),
                                                fontSize = 12.sp,
                                                color = Color.Gray,
                                                textDecoration = TextDecoration.LineThrough
                                            )
                                        }
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    if (p.stock < 10) {
                                        Text(
                                            text = "Only " + p.stock + " left!",
                                            color = Color(0xFFEF6C00),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    } else if (p.stock < 50) {
                                        Text(text = "In stock", color = Color(0xFF2E7D32), fontSize = 12.sp)
                                    } else {
                                        Text(text = "Plenty in stock", color = Color(0xFF2E7D32), fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }

    if (showProfile) {
        AlertDialog(
            onDismissRequest = { showProfile = false },
            title = { Text(state.userName ?: "Profile") },
            text = {
                Column {
                    if (state.userImage != null) {
                        AsyncImage(
                            model = state.userImage,
                            contentDescription = null,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                        )
                        Spacer(Modifier.height(12.dp))
                    }
                    Text("Items in cart: " + state.cartCount)
                    Text("Products loaded: " + state.products.size, color = Color.Gray, fontSize = 12.sp)
                }
            },
            confirmButton = {
                Button(onClick = {
                    vm.out()
                    showProfile = false
                    onLogout()
                }) { Text("Log out") }
            },
            dismissButton = {
                TextButton(onClick = { showProfile = false }) { Text("Close") }
            }
        )
    }
}
