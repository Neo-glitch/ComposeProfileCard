package com.neo.profilecardlayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.neo.profilecardlayout.ui.theme.MyTheme
import com.neo.profilecardlayout.ui.theme.lightGreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                UsersApplication()
            }
        }
    }
}

/**
 * composable to manage user navigation
 */
@Composable
fun UsersApplication(userProfiles: List<UserProfile> = userProfileList) {
    val navController = rememberNavController() // allows us control navigation
    NavHost(navController = navController, startDestination = "users_list") {
        // build the nav Graph
        composable("users_list") {
            //composable serves as fragment in this case
            UsersListScreen(userProfiles, navController)
        }

        // passing arg to a composable screen
        composable("user_details/{userId}",
            arguments = listOf(navArgument("userId"){
                type = NavType.IntType  // type of arg passed
            })) {navBackStackEntry ->
            // pass the arg to the UserDetailsScreen
            UserProfileDetailsScreen(navBackStackEntry.arguments!!.getInt("userId"), navController)
        }
    }
}

@Composable
fun UsersListScreen(userProfiles: List<UserProfile>, navController: NavController?) {
    Scaffold(
        topBar = { AppBar("Users List", Icons.Default.Home){} }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn() {
                items(userProfiles) { userProfile ->
                    ProfileCard(userProfile = userProfile){
                        navController?.navigate("user_details/${userProfile.id}")  // navigate to composable with this route
                    }
                }

            }
        }
    }
}

@Composable
fun AppBar(title: String, icon: ImageVector, iconClickAction: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            Icon(
                icon, contentDescription = null,
                modifier = Modifier.padding(horizontal = 12.dp)
                    .clickable { iconClickAction.invoke() }
            )
        },
        title = { Text(text = title) }
    )
}

@Composable
fun ProfileCard(userProfile: UserProfile, clickAction: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 4.dp, start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.Top)
            .clickable {
                clickAction.invoke()
            },
        elevation = 8.dp,
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            ProfilePicture(userProfile.pictureUrl, userProfile.status, 72.dp)
            ProfileContent(userProfile.name, userProfile.status, Alignment.Start)
        }

    }
}


@Composable
fun ProfilePicture(picture: String, onlineStatus: Boolean, imageSize: Dp) {
    Card(
        shape = CircleShape,
        border = BorderStroke(
            width = 2.dp,
            color = if (onlineStatus) MaterialTheme.colors.lightGreen else Color.Red
        ),
        modifier = Modifier.padding(16.dp),
        elevation = 4.dp
    ) {

//        // loads image syncronously
//        Image(
//            painter = painterResource(id = drawableId),
//            contentDescription = "content description",
//            contentScale = ContentScale.Crop  // to crop images to fit in our modifierSize specified
//        )

        // loads image async
        Image(
            painter = rememberImagePainter(
                data = picture,
                builder = {
                    transformations(CircleCropTransformation())
                }),
            contentDescription = null,
            modifier = Modifier.size(imageSize),
        )
    }
}

@Composable
fun ProfileContent(userName: String, onlineStatus: Boolean, alignment: Alignment.Horizontal) {
    Column(
        modifier = Modifier
            .padding(8.dp),
        horizontalAlignment = alignment
    ) {

        CompositionLocalProvider(
            LocalContentAlpha
                    provides (if (onlineStatus) ContentAlpha.high else ContentAlpha.disabled)
        ) {
            Text(userName, style = MaterialTheme.typography.h5)
        }
        // any ui widget in here has it's transparency changed to medium(0.74f)
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = if (onlineStatus) "Active Now" else "Offline",
                style = MaterialTheme.typography.body2
            )
        }
    }
}


@Composable
fun UserProfileDetailsScreen(userId: Int, navController: NavController?) {
    // loops and gets first element that satisfies this condition
    val userProfile = userProfileList.first {userProfile ->  userId == userProfile.id}

    Scaffold(
        topBar = { AppBar("User Profile Details", Icons.Default.ArrowBack){
           navController?.popBackStack() // pop backstack and go back
        } }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                ProfilePicture(userProfile.pictureUrl, userProfile.status, 240.dp)
                ProfileContent(userProfile.name, userProfile.status, Alignment.CenterHorizontally)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileDetailsProfilePreview() {
    MyTheme {
        UserProfileDetailsScreen(4, null)
    }
}

@Preview(showBackground = true)
@Composable
fun UserListPreview() {
    MyTheme {
        UsersListScreen(userProfiles = userProfileList, navController = null)
    }
}