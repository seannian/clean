import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (String) -> Unit,
    placeholderText: String = "",
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = MaterialTheme.colors.onSurface,
) {
    var textState by remember { mutableStateOf(TextFieldValue(query)) }

    TextField(
        value = textState,
        onValueChange = {
            textState = it
            onQueryChange(it.text)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp)),
        placeholder = {
            Text(placeholderText, color = contentColor.copy(alpha = 0.6f))
        },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search Icon", tint = contentColor)
        },
        trailingIcon = {
            if (textState.text.isNotEmpty()) {
                IconButton(onClick = {
                    textState = TextFieldValue("")
                    onQueryChange("")
                }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear Icon", tint = contentColor)
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = contentColor,
            textColor = contentColor
        )
    )
}
