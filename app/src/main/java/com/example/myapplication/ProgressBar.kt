import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.BratGreen

@Composable
fun LinearProgressBar(progress: Float) {
    var curProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (curProgress < progress) {
            curProgress += 0.01f
            kotlinx.coroutines.delay(10)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .height(24.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray)
    ) {
        LinearProgressIndicator(
            progress = { curProgress },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .height(24.dp),
            color = BratGreen,
            trackColor = Color.Transparent,
        )
    }
}