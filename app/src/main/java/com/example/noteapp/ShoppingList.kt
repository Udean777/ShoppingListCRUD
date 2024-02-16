package com.example.noteapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id: Int,
    var name: String,
    var qty: Int,
    var isEditing: Boolean = false
)

@Composable
fun ShoppingApp() {
    var sItems by remember{
        mutableStateOf(listOf<ShoppingItem>())
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var itemName by remember {
        mutableStateOf("")
    }
    var itemQty by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItems){item ->
                if(item.isEditing){
                    ShoppingEdit(item = item, onEditComplete = { editedName, editedQuantity ->
                        sItems = sItems.map {it.copy(isEditing = false) }
                        val editedItems = sItems.find { it.id == item.id }
                        editedItems?.let {
                            it.name = editedName
                            it.qty = editedQuantity
                        }
                    })
                }else{
                    ShoppingListItem(
                        item = item,
                        onEditClick = {
                        sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                    },
                        onDeleteClick = {
                        sItems = sItems-item
                    }
                    )
                }
            }
        }
    }

    if(showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = {
                        showDialog = false
                    }) {
                        Text(text = "Cancel")
                    }

                    Button(onClick = {
                        if(itemName.isNotBlank()){
                            val newItem = ShoppingItem(
                                id = sItems.size + 1,
                                name = itemName,
                                qty = itemQty.toInt(),
                            )
                            sItems = sItems + newItem
                            showDialog = false
                            itemName = ""
                            itemQty = ""
                        }
                    }) {
                        Text(text = "Add")
                    }
                }
            },
            title = { Text(text = "Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName, 
                        onValueChange = {itemName = it}, 
                        label = {
                            Text(text = "Item Name")
                        },
                        placeholder = {
                            Text(text = "Type your item name")
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    OutlinedTextField(
                        value = itemQty,
                        onValueChange = {itemQty = it},
                        label = {
                            Text(text = "Item Quantity")
                        },
                        placeholder = {
                            Text(text = "Type your item quantity")
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        )
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () ->Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(10)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = item.qty.toString(),
            modifier = Modifier.padding(8.dp)
        )
        
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
            }

            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
fun ShoppingEdit(
    item: ShoppingItem,
    onEditComplete: (String, Int) -> Unit
) {
    var editedName by remember {
        mutableStateOf(item.name)
    }

    var editedQuantity by remember {
        mutableStateOf(item.qty.toString())
    }

    var isEditing by remember {
        mutableStateOf(item.isEditing)
    }

//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        horizontalArrangement = Arrangement.SpaceEvenly,
////        verticalAlignment = Alignment.CenterVertically
//    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = editedName,
                onValueChange = {editedName = it},
                label = {
                    Text(text = "Item Name")
                },
                placeholder = {
                    Text(text = "Edit your item name")
                },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize()
            )

            OutlinedTextField(
                value = editedQuantity,
                onValueChange = {editedQuantity = it},
                label = {
                    Text(text = "Item Quantity")
                },
                placeholder = {
                    Text(text = "Edit your item quantity")
                },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .wrapContentSize()
            )

            Button(onClick = {
                isEditing = false
                onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
            }) {
                Text(text = "Save")
            }
        }
    }
//}