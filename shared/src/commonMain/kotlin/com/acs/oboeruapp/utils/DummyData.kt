package com.acs.oboeruapp.utils

import com.acs.oboeruapp.data.model.Deck
import com.acs.oboeruapp.data.model.DeckBlock
import com.acs.oboeruapp.data.model.DeckHeader
import com.acs.oboeruapp.data.model.LearningSpace
import com.acs.oboeruapp.data.model.LearningSpaceTemplate
import oboeruapp.shared.generated.resources.Res
import oboeruapp.shared.generated.resources.fan
import oboeruapp.shared.generated.resources.fuji_mountain
import oboeruapp.shared.generated.resources.ic_grammar
import oboeruapp.shared.generated.resources.ic_kanji
import oboeruapp.shared.generated.resources.ic_timer
import oboeruapp.shared.generated.resources.ic_vocabulary
import oboeruapp.shared.generated.resources.ramen
import oboeruapp.shared.generated.resources.torii_gate

val dummyLearningSpaces = listOf(
    LearningSpace(
        id = "ns_prep",
        title = "JLPT N5 Preparation"
    ),
    LearningSpace(
        id = "daily",
        title = "Daily"
    )
)

val dummyDecks = mapOf(
    "ns_prep" to Deck(
        header = DeckHeader(
            title = "JLPT N5 Preparation",
            description = "My journey to pass JLPT N5!",
            backgroundImageUrl = "https://images.unsplash.com/photo-1573416033034-e42e14b545d2?q=80&w=1470&auto=format&fit=crop",
        ),
        blocks = listOf(
            DeckBlock.ProgressBlock(
                id = "vocab",
                icon = Res.drawable.ic_vocabulary,
                title = "Vocabulary",
                level = "N5",
                current = 6,
                target = 20,
            ),
            DeckBlock.ProgressBlock(
                id = "kanji",
                icon = Res.drawable.ic_kanji,
                title = "Kanji",
                level = "N5",
                current = 3,
                target = 10,
            ),
            DeckBlock.ProgressBlock(
                id = "grammar",
                icon = Res.drawable.ic_grammar,
                title = "Grammar",
                level = "N5",
                current = 2,
                target = 10,
            ),
            DeckBlock.SimpleBlock(
                id = "review",
                icon = Res.drawable.ic_timer,
                title = "Today's Review",
                subtitle = "18 items due",
            )
        ),
    ),
    "daily" to Deck(
        header = DeckHeader(
            title = "Daily Practice",
            description = "Sedikit demi sedikit tiap hari",
            backgroundImageUrl = "https://images.unsplash.com/photo-1578632767115-351597cf2477?q=80&w=1470&auto=format&fit=crop",
        ),
        blocks = listOf(
            DeckBlock.ProgressBlock(
                id = "vocab",
                icon = Res.drawable.ic_vocabulary,
                title = "Vocabulary",
                level = "Daily",
                current = 4,
                target = 15,
            ),
            DeckBlock.SimpleBlock(
                id = "review",
                icon = Res.drawable.ic_timer,
                title = "Today's Review",
                subtitle = "5 items due",
            ),
        ),
    )
)

val dummyLearningSpaceTemplate = listOf(
    LearningSpaceTemplate(
        id = "ns_prep",
        title = "JLPT N5 Preparation",
        description = "Essential vocabulary, kanji, and grammar for N5.",
        icon = Res.drawable.fuji_mountain,
    ),
    LearningSpaceTemplate(
        id = "daily",
        title = "Daily Japanese",
        description = "Useful phrases for everyday situations.",
        icon = Res.drawable.ramen,
    ),
    LearningSpaceTemplate(
        id = "travel",
        title = "Travel in Japan",
        description = "Essential phrases for your trip.",
        icon = Res.drawable.torii_gate,
    ),
    LearningSpaceTemplate(
        id = "anime",
        title = "Anime & Manga",
        description = "Learn the language you love from anime and manga.",
        icon = Res.drawable.fan,
    ),
)