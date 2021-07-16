package com.jetpack.compose.learning.navigationdrawer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ModalDrawerActivity : ComponentActivity() {

    lateinit var drawerState: DrawerState
    lateinit var scope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ModalDrawerSample()
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Preview
    @Composable
    fun ModalDrawerSample() {
        drawerState = rememberDrawerState(DrawerValue.Closed)
        scope = rememberCoroutineScope()
        val (isGestureEnable, toggleGesturesEnabled) = remember { mutableStateOf(true) }
            ModalDrawer(
                drawerState = drawerState,
                gesturesEnabled = isGestureEnable,
                drawerContent = {
                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 16.dp),
                        onClick = { scope.launch { drawerState.close() } },
                        content = { Text("Close Drawer") }
                    )
                    LazyColumn {
                        items(10) {
                            ListItem(
                                text = { Text("Item ${it + 1}") },
                                icon = {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = "Description"
                                    )
                                }
                            )
                        }
                    }
                },
                content = {
                    Column {
                        TopAppBar(
                            title = { Text("Modal Drawer Sample") },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() }}) {
                                    Icon(Icons.Filled.Menu, contentDescription = null)
                                }
                            }
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .toggleable(
                                        value = isGestureEnable,
                                        onValueChange = toggleGesturesEnabled
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Checkbox(checked = isGestureEnable, null)
                                Text(
                                    text = "Enable swipe gesture",
                                    Modifier.padding(start = 5.dp),
                                    fontSize = 16.sp
                                )
                            }
                            Spacer(Modifier.height(20.dp))
                            Button(onClick = { scope.launch { drawerState.open() } }) {
                                Text("Click to open Drawer", fontSize = 16.sp)
                            }
                        }
                    }
                }
            )
    }

    override fun onBackPressed() {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else {
            super.onBackPressed()
        }
    }
}