/*
 * Cubiq Launcher
 * Minecraft Java Edition Landscape PC Layout Replica
 * Strictly matching user design specifications (Cubiq Launcher Landscape UI Collage)
 */

package com.movtery.zalithlauncher.ui.screens.content

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.movtery.zalithlauncher.ui.components.verticalScrollWithBar
import com.movtery.zalithlauncher.R
import com.movtery.zalithlauncher.game.account.Account
import com.movtery.zalithlauncher.game.account.AccountsManager
import com.movtery.zalithlauncher.game.account.getAccountTypeName
import com.movtery.zalithlauncher.game.version.installed.Version
import com.movtery.zalithlauncher.game.version.installed.VersionsManager
import com.movtery.zalithlauncher.path.PathManager
import com.movtery.zalithlauncher.ui.components.MinecraftButton
import com.movtery.zalithlauncher.ui.components.MinecraftButtonStyle
import com.movtery.zalithlauncher.ui.screens.NormalNavKey
import com.movtery.zalithlauncher.ui.screens.content.elements.CommonVersionInfoLayout
import com.movtery.zalithlauncher.ui.screens.content.elements.PlayerFace
import com.movtery.zalithlauncher.ui.screens.content.minecraft.InstallationDownloadManager
import com.movtery.zalithlauncher.ui.screens.content.minecraft.MinecraftInstallationsScreen
import com.movtery.zalithlauncher.ui.screens.content.minecraft.MinecraftModsScreen
import com.movtery.zalithlauncher.ui.screens.content.minecraft.MinecraftNewsScreen
import com.movtery.zalithlauncher.ui.screens.content.minecraft.MinecraftProfileScreen
import com.movtery.zalithlauncher.ui.screens.content.minecraft.MinecraftSkinsScreen
import com.movtery.zalithlauncher.ui.screens.content.minecraft.MinecraftPatchNotesScreen
import com.movtery.zalithlauncher.ui.theme.MinecraftFontFamily
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.movtery.zalithlauncher.game.versioninfo.MinecraftOfficialContentManager
import com.movtery.zalithlauncher.viewmodel.ScreenBackStackViewModel

/**
 * Ender Launcher landscape workspace with original navigation and layout:
 * - Left vertical sidebar: Avatar, Username, Status, Play, Installations, Skins, Patch Notes, News, Versions, Mods, Profile
 * - Top-right: FILES & MULTIPLAYER buttons
 * - Main content: Play screen hero banner + floating bottom dock, or the respective sub-screen
 */
@Composable
fun LandscapeLauncherLayout(
    account: Account?,
    accountsList: List<Account>,
    currentVersion: Version?,
    allVersions: List<Version>,
    isRefreshingVersions: Boolean,
    selectedBottomNav: Int,
    activeTab: Int,
    onSelectTab: (bottomNav: Int, tab: Int) -> Unit,
    handlePlayClick: () -> Unit,
    handlePlayVersion: (Version) -> Unit,
    backStackViewModel: ScreenBackStackViewModel,
    navigateToVersions: (Version) -> Unit,
    onLaunchGame: (Version?) -> Unit,
    onOpenLink: (String) -> Unit
) {
    val context = LocalContext.current
    val selectedPage = when {
        selectedBottomNav == 0 -> 4
        selectedBottomNav == 2 -> 5
        selectedBottomNav == 3 -> 6
        selectedBottomNav == 1 && activeTab == 0 -> 1
        selectedBottomNav == 1 && activeTab == 1 -> 2
        selectedBottomNav == 1 && activeTab == 2 -> 3
        else -> 0
    }
    val pageTitle = listOf("PORTAL", "INSTALAÇÕES", "CAPAS", "NOVIDADES", "NOTÍCIAS", "MODS", "JOGADOR")
        .getOrElse(selectedPage) { "PORTAL" }
    val onOpenFiles: () -> Unit = {
        com.movtery.zalithlauncher.filemanager.FileManagerLauncher.launch(
            context = context,
            rootPath = PathManager.DIR_FILES_EXTERNAL.absolutePath,
            currentPath = null,
            logsDir = PathManager.DIR_LAUNCHER_LOGS.absolutePath
        )
    }
    val onOpenMultiplayer: () -> Unit = {
        backStackViewModel.mainScreen.removeAndNavigateTo(
            removes = backStackViewModel.clearBeforeNavKeys,
            screenKey = NormalNavKey.Multiplayer
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090711))
    ) {
        // Ender identity header: a compact top command bar, not the inherited Cubiq sidebar.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .background(Color(0xFF100B1B))
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_ender_launcher),
                contentDescription = "Ender Launcher",
                modifier = Modifier.size(38.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text("ENDER", color = Color(0xFFE5C7FF), fontSize = 17.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                Text("JAVA MOBILE • THE END", color = Color(0xFF9C83B4), fontSize = 9.sp, letterSpacing = 1.5.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(pageTitle, color = Color(0xFFCC9BFF), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFF21152E))
                    .clickable { onSelectTab(3, -1) }
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val skinFile = account?.getSkinFile()
                if (account != null && skinFile != null && skinFile.exists()) {
                    PlayerFace(account = account, avatarSize = 27.dp)
                } else {
                    Image(painter = painterResource(R.drawable.ic_mc_pc_profile), contentDescription = null, modifier = Modifier.size(27.dp))
                }
                Text(account?.username ?: "Convidado", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            }
        }

        // Horizontal navigation rail with a distinct Ender visual language.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF171020))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EnderNavChip("JOGAR", selectedPage == 0) { onSelectTab(1, -1) }
            EnderNavChip("INSTALAÇÕES", selectedPage == 1) { onSelectTab(1, 0) }
            EnderNavChip("CAPAS", selectedPage == 2) { onSelectTab(1, 1) }
            EnderNavChip("ATUALIZAÇÕES", selectedPage == 3) { onSelectTab(1, 2) }
            EnderNavChip("NOTÍCIAS", selectedPage == 4) { onSelectTab(0, -1) }
            EnderNavChip("MODS", selectedPage == 5) { onSelectTab(2, -1) }
            EnderNavChip("PERFIL", selectedPage == 6) { onSelectTab(3, -1) }
            Spacer(modifier = Modifier.weight(1f))
            EnderNavChip("ARQUIVOS", false, onOpenFiles)
            EnderNavChip("MULTIJOGADOR", false, onOpenMultiplayer)
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when (selectedPage) {
                1 -> MinecraftInstallationsScreen(
                    currentVersion = currentVersion,
                    backStackViewModel = backStackViewModel,
                    navigateToVersions = navigateToVersions,
                    onLaunchGame = onLaunchGame,
                    onBackClick = { onSelectTab(1, -1) }
                )
                2 -> MinecraftSkinsScreen(account = account, onBackClick = { onSelectTab(1, -1) })
                3 -> MinecraftPatchNotesScreen(onOpenLink = onOpenLink, onBackClick = { onSelectTab(1, -1) })
                4 -> MinecraftNewsScreen(onOpenLink = onOpenLink, onBackClick = { onSelectTab(1, -1) })
                5 -> MinecraftModsScreen(
                    currentVersion = currentVersion,
                    backStackViewModel = backStackViewModel,
                    navigateToVersions = navigateToVersions,
                    onBackClick = { onSelectTab(1, -1) }
                )
                6 -> MinecraftProfileScreen(
                    account = account,
                    backStackViewModel = backStackViewModel,
                    onBackClick = { onSelectTab(1, -1) }
                )
                else -> {
                    EnderPortalHome(
                        account = account,
                        currentVersion = currentVersion,
                        allVersions = allVersions,
                        isRefreshingVersions = isRefreshingVersions,
                        onPlay = handlePlayClick,
                        onManageInstallations = { onSelectTab(1, 0) },
                        onSelectVersion = { version -> VersionsManager.saveVersion(version) },
                        onPlayVersion = handlePlayVersion
                    )
                }
            }
            if (selectedPage != 0) {
                Box(
                    modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    CompactBackgroundDownloadBar(onExpand = { InstallationDownloadManager.showDialog() })
                }
            }
        }
    }
}

@Composable
private fun EnderNavChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(9.dp)
    Row(
        modifier = Modifier
            .clip(shape)
            .background(if (selected) Color(0xFF49246B) else Color(0xFF21182B))
            .border(BorderStroke(1.dp, if (selected) Color(0xFFC58AFF) else Color(0xFF392747)), shape)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = if (selected) Color.White else Color(0xFFB9A7C8), fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.7.sp)
    }
}

@Composable
private fun EnderPortalHome(
    account: Account?,
    currentVersion: Version?,
    allVersions: List<Version>,
    isRefreshingVersions: Boolean,
    onPlay: () -> Unit,
    onManageInstallations: () -> Unit,
    onSelectVersion: (Version) -> Unit,
    onPlayVersion: (Version) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize().padding(22.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Minimal account panel: no promotional hero, news, or extra dashboard tiles.
        Column(
            modifier = Modifier.weight(0.34f).fillMaxHeight()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF171020))
                .border(BorderStroke(1.dp, Color(0xFF49305D)), RoundedCornerShape(18.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("SUA CONTA", color = Color(0xFFCDA7EC), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Box(
                modifier = Modifier.size(76.dp).clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF2A1B36))
                    .border(BorderStroke(2.dp, Color(0xFF9A5BC7)), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                val skinFile = account?.getSkinFile()
                if (account != null && skinFile != null && skinFile.exists()) {
                    PlayerFace(account = account, avatarSize = 70.dp)
                } else {
                    Image(
                        painter = painterResource(R.drawable.ic_mc_pc_profile),
                        contentDescription = "Avatar",
                        modifier = Modifier.size(66.dp)
                    )
                }
            }
            Text(
                account?.username ?: "Nenhuma conta",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                account?.let { getAccountTypeName(it) } ?: "Adicione ou selecione uma conta no Perfil",
                color = Color(0xFFAA98B8),
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                if (currentVersion != null) "Selecionada: ${currentVersion.getVersionName()}" else "Nenhuma versão selecionada",
                color = Color(0xFFD7C5E5),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Installed versions are the only main content beside the account card.
        Column(
            modifier = Modifier.weight(0.66f).fillMaxHeight()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF120D19))
                .border(BorderStroke(1.dp, Color(0xFF49305D)), RoundedCornerShape(18.dp))
                .padding(18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("SUAS VERSÕES", color = Color(0xFFE2C8F6), fontSize = 15.sp, fontWeight = FontWeight.Black, letterSpacing = 1.4.sp)
                    Text("${allVersions.size} instalação(ões) disponíveis", color = Color(0xFF9E8BAE), fontSize = 10.sp)
                }
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(9.dp))
                        .background(Color(0xFF713A98))
                        .clickable(onClick = onManageInstallations)
                        .padding(horizontal = 14.dp, vertical = 9.dp)
                ) {
                    Text("+ ADICIONAR", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            if (allVersions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhuma versão instalada. Use + ADICIONAR para começar.", color = Color(0xFFAA98B8), fontSize = 12.sp)
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allVersions.forEach { version ->
                        val isSelected = currentVersion?.getVersionName() == version.getVersionName()
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFF2A1B38) else Color(0xFF1B1423))
                                .border(BorderStroke(1.dp, if (isSelected) Color(0xFFB56CFF) else Color(0xFF382746)), RoundedCornerShape(12.dp))
                                .clickable { onSelectVersion(version) }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CommonVersionInfoLayout(
                                modifier = Modifier.weight(1f),
                                version = version,
                                iconSize = 34.dp
                            )
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(8.dp))
                                    .background(Brush.horizontalGradient(listOf(Color(0xFF9852C5), Color(0xFF62318A))))
                                    .clickable { onSelectVersion(version); onPlayVersion(version) }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Text("JOGAR", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EnderInfoTile(title: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxHeight().clip(RoundedCornerShape(13.dp))
            .background(Color(0xFF171020)).border(BorderStroke(1.dp, Color(0xFF392747)), RoundedCornerShape(13.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, color = Color(0xFF9F83B7), fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Text(value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
    }
}

/**
 * Landscape Play Screen matching Image 2 top-left:
 * - Full hero art banner
 * - Centered Minecraft Java Edition logo
 * - Top-right FILES & MULTIPLAYER buttons
 * - Floating bottom dock with Selected Version card & Green PLAY button
 */
/**
 * Landscape Play Screen:
 * - Centered high-resolution Wilderness Bound hero art banner
 * - Centered Minecraft Java Edition logo
 * - Top-right Screen Rotation, FILES & MULTIPLAYER buttons
 * - Floating bottom dock with Selected Version card & textured Green PLAY button
 */
@Composable
private fun LandscapePlayScreen(
    currentVersion: Version?,
    allVersions: List<Version>,
    account: Account?,
    isRefreshingVersions: Boolean,
    showVersionMenu: Boolean,
    onShowVersionMenuChange: (Boolean) -> Unit,
    onPlayClick: () -> Unit,
    onManageInstallations: () -> Unit,
    onOpenFiles: () -> Unit,
    onOpenMultiplayer: () -> Unit,
    onSelectTab: (bottomNav: Int, tab: Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // High-res portrait artwork centered in landscape
        Image(
            painter = painterResource(R.drawable.img_hero_wilderness),
            contentDescription = "Minecraft Java Edition",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        // Gradient overlay for contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        0.55f to Color.Transparent,
                        0.80f to Color(0x99000000),
                        1.0f to Color(0xEE0B0C0E)
                    )
                )
        )

        // Top-Right Actions: FILES, MULTIPLAYER, Rotation
        LandscapeTopRightActions(
            onOpenFiles = onOpenFiles,
            onOpenMultiplayer = onOpenMultiplayer,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 10.dp, end = 12.dp)
        )

        // Centered Minecraft Java Edition Logo
        Image(
            painter = painterResource(R.drawable.img_mc_java_logo),
            contentDescription = "Minecraft Java Edition",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 52.dp)
                .height(76.dp)
                .widthIn(max = 340.dp),
            contentScale = ContentScale.Fit
        )

        // Floating Bottom Dock
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Background Download Bar if downloading
                CompactBackgroundDownloadBar(
                    onExpand = {
                        onSelectTab(1, 0)
                        InstallationDownloadManager.showDialog()
                    }
                )

                // Dock Container
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Selected Version Card
                    val latestReleaseVersion by MinecraftOfficialContentManager.latestReleaseVersion.collectAsStateWithLifecycle()
                    val activeVer = currentVersion ?: allVersions.firstOrNull()
                    val mcVer = activeVer?.getVersionInfo()?.minecraftVersion
                    val verTitle = if (activeVer == null) "Latest Release" else "Selected Version"
                    val verDisplay = if (mcVer != null && activeVer != null) {
                        "${activeVer.getVersionName()} ($mcVer)"
                    } else {
                        activeVer?.getVersionName() ?: latestReleaseVersion
                    }
                    val verIconFile = activeVer?.let { com.movtery.zalithlauncher.game.version.installed.getVersionIconFile(it.getVersionPath()) }?.takeIf { it.exists() }

                    Box {
                        Row(
                            modifier = Modifier
                                .height(48.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF16181B))
                                .border(BorderStroke(1.5.dp, Color(0xFF32353A)), RoundedCornerShape(6.dp))
                            .clickable {
                                com.movtery.zalithlauncher.ui.sound.MinecraftSoundHelper.playClickSound()
                                onShowVersionMenuChange(true)
                            }
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (verIconFile != null) {
                            coil3.compose.AsyncImage(
                                model = verIconFile,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.block_crafting_table),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Column {
                            Text(
                                text = verTitle,
                                color = Color(0xFFAAAAAA),
                                fontFamily = MinecraftFontFamily,
                                fontSize = 10.sp
                            )
                            Text(
                                text = verDisplay,
                                color = Color.White,
                                fontFamily = MinecraftFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_drop_down_rounded),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Version Dropdown
                    DropdownMenu(
                        expanded = showVersionMenu,
                        onDismissRequest = { onShowVersionMenuChange(false) },
                        modifier = Modifier
                            .width(280.dp)
                            .background(Color(0xFF1C1C1E))
                            .border(BorderStroke(1.5.dp, Color(0xFF383838)), RoundedCornerShape(3.dp))
                    ) {
                        if (allVersions.isEmpty()) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Latest Release ($latestReleaseVersion)",
                                        color = Color.White,
                                        fontFamily = MinecraftFontFamily,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                onClick = {
                                    onShowVersionMenuChange(false)
                                    onManageInstallations()
                                }
                            )
                        } else {
                                allVersions.forEach { ver ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                CommonVersionInfoLayout(
                                                    modifier = Modifier.weight(1f),
                                                    version = ver,
                                                    iconSize = 22.dp
                                                )
                                            }
                                        },
                                        onClick = {
                                            VersionsManager.saveVersion(ver)
                                            onShowVersionMenuChange(false)
                                        }
                                    )
                                }
                            }

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "+ Manage Installations",
                                        color = Color(0xFF55FF55),
                                        fontFamily = MinecraftFontFamily,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                onClick = {
                                    onShowVersionMenuChange(false)
                                    onManageInstallations()
                                }
                            )
                        }
                    }

                    // Right: Textured Minecraft Green PLAY Button
                    MinecraftButton(
                        onClick = {
                            com.movtery.zalithlauncher.ui.sound.MinecraftSoundHelper.playClickSound()
                            onPlayClick()
                        },
                        style = MinecraftButtonStyle.GREEN,
                        isLoading = isRefreshingVersions,
                        text = "PLAY",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .width(220.dp)
                            .height(48.dp)
                    )
                }
            }
        }
    }
}

/**
 * Top right action buttons: Rotation lock toggle, FILES & MULTIPLAYER
 */
@Composable
private fun LandscapeTopRightActions(
    onOpenFiles: () -> Unit,
    onOpenMultiplayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = context as? android.app.Activity

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // FILES Button
        MinecraftButton(
            onClick = {
                com.movtery.zalithlauncher.ui.sound.MinecraftSoundHelper.playClickSound()
                onOpenFiles()
            },
            style = MinecraftButtonStyle.STONE,
            text = "FILES",
            fontSize = 11.sp,
            modifier = Modifier
                .height(32.dp)
                .widthIn(min = 64.dp)
        )

        // MULTIPLAYER Button
        MinecraftButton(
            onClick = {
                com.movtery.zalithlauncher.ui.sound.MinecraftSoundHelper.playClickSound()
                onOpenMultiplayer()
            },
            style = MinecraftButtonStyle.GREEN,
            text = "MULTIPLAYER",
            fontSize = 11.sp,
            modifier = Modifier
                .height(32.dp)
                .widthIn(min = 96.dp)
        )

        // Orientation Toggle Button (placed at far right edge)
        MinecraftButton(
            onClick = {
                com.movtery.zalithlauncher.ui.sound.MinecraftSoundHelper.playClickSound()
                val currentOrientation = activity?.resources?.configuration?.orientation
                if (currentOrientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
                    activity?.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    com.movtery.zalithlauncher.setting.AllSettings.launcherOrientationLock.save("landscape")
                } else {
                    activity?.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    com.movtery.zalithlauncher.setting.AllSettings.launcherOrientationLock.save("portrait")
                }
            },
            style = MinecraftButtonStyle.STONE,
            modifier = Modifier
                .height(32.dp)
                .width(36.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_screen_rotation),
                contentDescription = "Rotate Screen",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Navigation button in the left sidebar with authentic pixel icons
 */
@Composable
private fun LandscapeNavButton(
    label: String,
    iconRes: Int,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val bgModifier = if (isActive) {
        Modifier
            .background(Color(0xFF1E3A19), RoundedCornerShape(6.dp))
            .border(BorderStroke(1.5.dp, Color(0xFF55FF55)), RoundedCornerShape(6.dp))
    } else {
        Modifier.background(Color.Transparent)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .then(bgModifier)
            .clickable {
                com.movtery.zalithlauncher.ui.sound.MinecraftSoundHelper.playClickSound()
                onClick()
            }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = label,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = label,
            color = if (isActive) Color.White else Color(0xFFAAAAAA),
            fontFamily = MinecraftFontFamily,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            fontSize = 13.sp
        )
    }
}

/**
 * Startup Unofficial Fork Notice Modal Dialog (Requirement #11)
 */
@Composable
fun ForkWarningDialog(
    onDismiss: () -> Unit,
    onDontShowAgain: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF1E1E20))
                .border(BorderStroke(1.5.dp, Color(0xFF383838)), RoundedCornerShape(4.dp))
                .padding(18.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header with Warning Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF3D3200))
                            .border(BorderStroke(1.dp, Color(0xFFFFCC00)), RoundedCornerShape(3.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chat_info),
                            contentDescription = null,
                            tint = Color(0xFFFFCC00),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "UNOFFICIAL FORK NOTICE",
                            color = Color.White,
                            fontFamily = MinecraftFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "GPLv3 Open Source Attribution",
                            color = Color(0xFFAAAAAA),
                            fontFamily = MinecraftFontFamily,
                            fontSize = 10.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF333333))
                )

                // Body content
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Ender Launcher is an independent, unofficial fork based on Zalith Launcher 2, licensed under the GNU General Public License v3.0 (GPLv3).",
                        color = Color(0xFFE2E2E2),
                        fontFamily = MinecraftFontFamily,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF161618))
                            .border(BorderStroke(1.dp, Color(0xFF2E2E32)), RoundedCornerShape(3.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "This launcher is NOT affiliated with, endorsed by, or supported by the original Zalith Launcher authors, Mojang Studios, or Microsoft Corporation.",
                            color = Color(0xFFFFAA00),
                            fontFamily = MinecraftFontFamily,
                            fontSize = 10.5.sp,
                            lineHeight = 15.sp
                        )
                    }

                    Text(
                        text = "All modifications and source code comply with GPLv3. For support or bug reports, please use the Ender Launcher project community.",
                        color = Color(0xFF888888),
                        fontFamily = MinecraftFontFamily,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Balanced Side-by-Side Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MinecraftButton(
                        onClick = onDontShowAgain,
                        text = "DON'T SHOW AGAIN",
                        style = MinecraftButtonStyle.STONE,
                        fontSize = 10.5.sp,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                    )

                    MinecraftButton(
                        onClick = onDismiss,
                        text = "I UNDERSTOOD",
                        style = MinecraftButtonStyle.GREEN,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp)
                    )
                }
            }
        }
    }
}
