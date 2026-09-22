package com.acs.oboeruapp.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.acs.oboeruapp.data.model.DeckBlock
import com.acs.oboeruapp.data.model.DeckHeader
import com.acs.oboeruapp.ui.components.AddBlockRow
import com.acs.oboeruapp.ui.components.DeckCard
import com.acs.oboeruapp.ui.components.UserHeader
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.ic_grammar
import oboeruapp.shared.generated.resources.ic_home
import oboeruapp.shared.generated.resources.ic_kanji
import oboeruapp.shared.generated.resources.ic_timer
import oboeruapp.shared.generated.resources.ic_vocabulary

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        UserHeader(
            isAuthenticated = false,
            photoUrl = null,
            displayName = null,
            onProfileClick = {},
            onNotificationClick = {},
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
        ) {
            item {
                Column {
                    DeckCard(
                        header = DeckHeader(
                            title = "JLPT N5 Preparation",
                            description = "My journey to pass JLPT N5!",
                            backgroundImageUrl = "https://images.unsplash.com/photo-1573416033034-e42e14b545d2?q=80&w=1470&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        ),
                        blocks = listOf(
                            DeckBlock.ProgressBlock("vocab", Res.drawable.ic_vocabulary, "Vocabulary", "N5", 6, 20),
                            DeckBlock.ProgressBlock("kanji", Res.drawable.ic_kanji, "Kanji", "N5", 3, 10),
                            DeckBlock.ProgressBlock("grammar", Res.drawable.ic_grammar, "Grammar", "N5", 2, 10),
                            DeckBlock.SimpleBlock("review", Res.drawable.ic_timer, "Today's Review", "18 items due")
                        ),
                        onMenuClick = { /* TODO */ },
                        onBlockClick = { block -> /* TODO: navigate sesuai block.id */ },
                        onAddBlockClick = { /* TODO */ },
                    )
                    Spacer(Modifier.height(12.dp))
                    AddBlockRow(onClick = { /* TODO */ })
                }
            }
        }
    }
}