package com.izamha.snacky.ui.home.profile

import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.izamha.snacky.R
import com.izamha.snacky.model.Snack
import com.izamha.snacky.model.SnackCollection
import com.izamha.snacky.model.SnackRepo
import com.izamha.snacky.ui.theme.Neutral8
import com.izamha.snacky.ui.theme.SnackyTheme
import com.izamha.snacky.ui.utils.mirroringBackIcon
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.izamha.snacky.ui.auth.LoginActivity
import com.izamha.snacky.ui.components.*
import com.izamha.snacky.ui.rememberAppStateHolder
import kotlin.math.max
import kotlin.math.min

private val BottomBarHeight = 56.dp
private val TitleHeight = 100.dp // 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 15.dp // 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 150.dp //300.dp
private val CollapsedImageSize = 75.dp //150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@Composable
fun Profile(
    modifier: Modifier = Modifier,
    snackId: Long
) {
    // val snack = remember(snackId) { SnackRepo.getSnack(snackId) }
    val related = remember(snackId) { SnackRepo.getRelated(snackId) }
    val navController = rememberNavController()
    val mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser

    Box(modifier.fillMaxSize()) {
        val scroll = rememberScrollState(0)
        val context = LocalContext.current

        NavHost(navController = navController, startDestination = "from-profile") {

            composable("from-profile") {
                NewHeader(navController = navController, upPress = { navController.navigateUp() })
            }

            composable("home/add-snack") {
                AddSnack(upPress = { navController.navigateUp() })
            }
        }

//        Header()
//        Body(related, scroll)
//        Title(snack, scroll.value)
//        Image(snack.imageUrl, scroll.value)
    }

}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
@Composable
fun NewHeader(
    modifier: Modifier = Modifier,
    imageUrl: String = "https://images.unsplash.com/photo-1516192518150-0d8fee5425e3?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=436&q=80",
    navController: NavHostController = rememberAppStateHolder().navController,
    upPress: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Spacer(modifier = modifier.size(56.dp))
//        Up(upPress)
        ProfileContent(imageUrl, navController = navController)
        RetrieveFirebaseContent()
    }
}


@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalComposeUiApi
@Composable
fun ProfileContent(
    imageUrl: String,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAppStateHolder().navController
) {

    val textState = remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current
    val database = Firebase.database
    val myRef = database.reference.child("Snack")

    val mAuth = FirebaseAuth.getInstance()
    val currentUser = mAuth.currentUser

    val isAdmin = remember { SnackRepo.getAdmin(currentUser?.email!!) }


    SnackyScaffold(
        floatingActionButton = {
            if (isAdmin) {
                FloatingActionButton(
                    onClick = {
                        try {
                            navController.navigate("home/add-snack")
                        } catch (e: java.lang.NullPointerException) {
                            Log.d("PROFILE", "NewHeader: ${e.localizedMessage}")
                        }
                    },
                    backgroundColor = SnackyTheme.colors.textPrimary,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        backgroundColor = Color.Unspecified,
    ) {

        LazyColumn(modifier = modifier.padding(vertical = 24.dp)) {
            item {

                Row(modifier = modifier.padding(16.dp)) {
                    SnackImage(
                        imageUrl = currentUser?.photoUrl?.toString()
                            ?: "https://images.unsplash.com/photo-1589149098258-3e9102cd63d3?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=739&q=80",
                        contentDescription = null,
                        modifier = modifier
                            .size(100.dp)
                    )

                    Column(
                        modifier = modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = modifier.padding(horizontal = 16.dp),
                            text = if (isAdmin) "Admin" else "Not Admin"
                        )
                        Text(
                            text = if (currentUser != null) currentUser.displayName!! else "Placeholder",
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.h5,
                            color = SnackyTheme.colors.textPrimary,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    top = 16.dp, bottom = 8.dp
                                )
                                .align(Alignment.Start)

                        )

                        Text(
                            text = "Ku bisima, KN 122st",
                            style = MaterialTheme.typography.body1,
                            fontSize = 16.sp,
                            color = SnackyTheme.colors.textPrimary,
                            modifier = modifier
                                .padding(start = 16.dp)
                                .align(Alignment.Start),
                        )

                    }

                }
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, start = 32.dp, bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        tint = SnackyTheme.colors.textPrimary,
                        contentDescription = stringResource(
                            id = R.string.work_in_progress
                        )
                    )
                    Text(
                        text = "(+250) 787-907-590",
                        style = MaterialTheme.typography.body1,
                        fontSize = 16.sp,
                        color = SnackyTheme.colors.textPrimary,
                        modifier = modifier.padding(start = 16.dp)
                    )
                }
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Email,
                        tint = SnackyTheme.colors.textPrimary,
                        contentDescription = stringResource(
                            id = R.string.work_in_progress
                        )
                    )
                    Text(
                        text = if (currentUser != null) currentUser.email!! else stringResource(id = R.string.placeholder_email),
                        style = MaterialTheme.typography.body1,
                        fontSize = 16.sp,
                        color = SnackyTheme.colors.textPrimary,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                SnackyDivider(
                    thickness = 1.dp, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                SnackyDivider(
                    thickness = 1.dp, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        tint = SnackyTheme.colors.textPrimary,
                        contentDescription = stringResource(
                            id = R.string.your_favorites
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.your_favorites),
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 16.sp,
                        color = SnackyTheme.colors.textPrimary,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, top = 24.dp)
                        .clickable {
                            snackyToast(context, "Payment")
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        tint = SnackyTheme.colors.textPrimary,
                        contentDescription = stringResource(
                            id = R.string.payment
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.payment),
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 16.sp,
                        color = SnackyTheme.colors.textPrimary,
                        modifier = Modifier
                            .padding(start = 16.dp)
                    )

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, top = 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PeopleOutline,
                        tint = SnackyTheme.colors.textPrimary,
                        contentDescription = stringResource(
                            id = R.string.tell_your_friend
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.tell_your_friend),
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 16.sp,
                        color = SnackyTheme.colors.textPrimary,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, top = 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Tab,
                        tint = SnackyTheme.colors.textPrimary,
                        contentDescription = stringResource(
                            id = R.string.promotions
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.promotions),
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 16.sp,
                        color = SnackyTheme.colors.textPrimary,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, top = 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        tint = SnackyTheme.colors.textPrimary,
                        contentDescription = stringResource(
                            id = R.string.work_in_progress
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = MaterialTheme.typography.subtitle1,
                        fontSize = 16.sp,
                        color = SnackyTheme.colors.textPrimary,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                SnackyDivider(
                    thickness = 1.dp, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, top = 16.dp),
                ) {
                    if (currentUser != null) {

                        TextButton(onClick = {
                            mAuth.signOut()
                            val loginIntent = Intent(context, LoginActivity::class.java)
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            context.startActivity(loginIntent)
                        }) {
                            Icon(
                                imageVector = Icons.Default.SettingsPower,
                                tint = Color.Red,
                                contentDescription = stringResource(
                                    id = R.string.logout
                                )
                            )
                            Text(
                                text = stringResource(id = R.string.logout),
                                style = MaterialTheme.typography.subtitle1,
                                fontSize = 16.sp,
                                color = Color.Red,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Login,
                            tint = SnackyTheme.colors.textPrimary,
                            contentDescription = stringResource(
                                id = R.string.login
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.login),
                            style = MaterialTheme.typography.subtitle1,
                            fontSize = 16.sp,
                            color = SnackyTheme.colors.textPrimary,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                }
            }
        }
    }
}


@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(Brush.horizontalGradient(SnackyTheme.colors.gradient2_1))
    )
}

@Composable
private fun Body(
    related: List<SnackCollection>,
    scroll: ScrollState
) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(GradientScroll))
            SnackySurface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(Modifier.height(ImageOverlap))
                    Spacer(Modifier.height(TitleHeight))

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.detail_header),
                        style = MaterialTheme.typography.overline,
                        color = SnackyTheme.colors.textHelp,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(16.dp))
                    var seeMore by remember { mutableStateOf(true) }
                    Text(
                        text = stringResource(R.string.detail_placeholder),
                        style = MaterialTheme.typography.body1,
                        color = SnackyTheme.colors.textHelp,
                        maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis,
                        modifier = HzPadding
                    )
                    val textButton = if (seeMore) {
                        stringResource(id = R.string.see_more)
                    } else {
                        stringResource(id = R.string.see_less)
                    }
                    Text(
                        text = textButton,
                        style = MaterialTheme.typography.button,
                        textAlign = TextAlign.Center,
                        color = SnackyTheme.colors.textLink,
                        modifier = Modifier
                            .heightIn(20.dp)
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clickable {
                                seeMore = !seeMore
                            }
                    )
                    Spacer(Modifier.height(40.dp))

                    SnackyDivider()

                    Spacer(
                        modifier = Modifier
                            .padding(bottom = BottomBarHeight)
                            .navigationBarsPadding(start = false, end = false)
                            .height(8.dp)
                    )
                }
            }
        }
    }
}


@ExperimentalCoilApi
@Composable
fun Image(imageUrl: String, scroll: Int) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFraction = (scroll / collapseRange).coerceIn(0f, 1f)

    CollapsingImageLayout(
        collapseFraction = collapseFraction,
        modifier = HzPadding.then(Modifier.statusBarsPadding())
    ) {
        SnackImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFraction: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            (constraints.maxWidth - imageWidth) / 2, // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = Neutral8.copy(alpha = 0.32f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = mirroringBackIcon(),
            tint = SnackyTheme.colors.iconInteractive,
            contentDescription = stringResource(R.string.label_back)
        )
    }
}

@Composable
fun Title(snack: Snack, scroll: Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }
    val offset = (maxOffset - scroll).coerceAtLeast(minOffset)

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .graphicsLayer { translationY = offset }
            .background(color = SnackyTheme.colors.uiBackground)
    ) {
        Text(
            text = "izamha",
            style = TextStyle(fontSize = 32.sp, color = SnackyTheme.colors.textHelp),
            modifier = Modifier
                .padding(24.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            textAlign = TextAlign.Center

        )
    }
}


@Composable
fun RetrieveFirebaseContent() {
    val database = Firebase.database
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val myRef = database.reference.child("Snacks").child(userId!!)
    val context = LocalContext.current

    // Read from the database
    myRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                for (ds in snapshot.children) {
                    val data = ds.getValue(Snack::class.java)
                    Log.d("FIREBASE", "onDataChange: ${data?.name}")
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })
}


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalCoilApi
@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
fun ProfilePreview() {
    SnackyTheme {
        Profile(snackId = 1L)
    }
}
