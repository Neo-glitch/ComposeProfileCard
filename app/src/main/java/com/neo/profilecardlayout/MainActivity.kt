package com.neo.profilecardlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neo.profilecardlayout.ui.theme.MyTheme
import com.neo.profilecardlayout.ui.theme.lightGreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme() {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(userProfiles: List<UserProfile> = userProfileList) {
    Scaffold(
        topBar = { AppBar() }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(){
                userProfiles.forEach { userProfile ->
                    ProfileCard(userProfile = userProfile)
                }
            }
        }
    }
}

@Composable
fun AppBar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                Icons.Default.Home, contentDescription = null,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        },
        title = { Text(text = "Messaging Application Users") }
    )
}

@Composable
fun ProfileCard(userProfile: UserProfile) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top),
        elevation = 8.dp,
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfilePicture(userProfile.drawableId, userProfile.status)
            ProfileContent(userProfile.name, userProfile.status)
        }

    }
}


@Composable
fun ProfilePicture(drawableId: Int, onlineStatus: Boolean) {
    Card(
        shape = CircleShape,
        border = BorderStroke(width = 2.dp,
            color = if (onlineStatus) MaterialTheme.colors.lightGreen else Color.Red),
        modifier = Modifier.padding(16.dp),
        elevation = 4.dp
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "content description",
            modifier = Modifier.size(72.dp),
            contentScale = ContentScale.Crop  // to crop images to fit in our modifierSize specified
        )
    }
}

@Composable
fun ProfileContent(userName: String, onlineStatus: Boolean) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()  // fills the remaining space left in parent
    ) {

        CompositionLocalProvider(LocalContentAlpha
                provides (if(onlineStatus) ContentAlpha.high else ContentAlpha.disabled)) {
            Text(userName, style = MaterialTheme.typography.h5)
        }
        // any ui widget in here has it's transparency changed to medium(0.74f)
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(text = if(onlineStatus) "Active Now" else "Offline",
                style = MaterialTheme.typography.body2)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyTheme() {
        MainScreen()
    }
}