package com.acs.oboeruapp.features.deck.domain.model

enum class DeckTopic(
    val title: String,
    val badgeLabel: String,
) {
    HIRAGANA(title = "Hiragana", badgeLabel = "Kana"),
    KATAKANA(title = "Katakana", badgeLabel = "Kana"),
    ALL_REVIEW(title = "Today's Review", badgeLabel = "SRS");

    companion object {
        fun fromString(value: String): DeckTopic =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: HIRAGANA
    }
}

enum class DeckActivityType(
    val label: String,
    val defaultDailyTarget: Int,
) {
    CHART(label = "Character Table", defaultDailyTarget = 0),
    FLASHCARD(label = "Flashcards", defaultDailyTarget = 15),
    QUIZ(label = "Quiz", defaultDailyTarget = 10),
    REVIEW(label = "Review", defaultDailyTarget = 0);

    companion object {
        fun fromString(value: String): DeckActivityType =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: CHART
    }
}

data class UserDeckBlock(
    val id: String,
    val topic: DeckTopic,
    val activityType: DeckActivityType,
    val dailyTarget: Int,
    val sortOrder: Int,
    val completedToday: Int = 0,
    val dueCount: Int = 0,
) {
    val isGoalBased: Boolean
        get() = dailyTarget > 0

    val isCompletedToday: Boolean
        get() = isGoalBased && completedToday >= dailyTarget

    val progressFraction: Float?
        get() = if (!isGoalBased) {
            null
        } else {
            (completedToday.toFloat() / dailyTarget.toFloat()).coerceIn(0f, 1f)
        }

    val displayTitle: String
        get() = if (activityType == DeckActivityType.REVIEW) {
            topic.title
        } else {
            "${topic.title} · ${activityType.label}"
        }
}

data class DailySummary(
    val completedGoals: Int = 0,
    val totalGoals: Int = 0,
    val streakDays: Int = 0,
)

data class BlockOptionSpec(
    val topic: DeckTopic,
    val activityType: DeckActivityType,
    val defaultTarget: Int = activityType.defaultDailyTarget,
)

data class StarterPack(
    val id: String,
    val title: String,
    val subtitle: String,
    val blocks: List<BlockOptionSpec>,
)

object DeckCatalog {

    val selectableTopics: List<DeckTopic> = listOf(
        DeckTopic.HIRAGANA,
        DeckTopic.KATAKANA,
        DeckTopic.ALL_REVIEW,
    )

    fun activitiesForTopic(topic: DeckTopic): List<DeckActivityType> =
        when (topic) {
            DeckTopic.HIRAGANA,
            DeckTopic.KATAKANA -> listOf(
                DeckActivityType.CHART,
                DeckActivityType.FLASHCARD,
                DeckActivityType.QUIZ,
            )

            DeckTopic.ALL_REVIEW -> listOf(DeckActivityType.REVIEW)
        }

    val dailyTargetPresets: List<Int> = listOf(5, 10, 15, 20, 30)

    val starterPacks: List<StarterPack> = listOf(
        StarterPack(
            id = "pack_hiragana",
            title = "Hiragana Starter",
            subtitle = "Table, Flashcards & Quiz",
            blocks = listOf(
                BlockOptionSpec(DeckTopic.HIRAGANA, DeckActivityType.CHART),
                BlockOptionSpec(DeckTopic.HIRAGANA, DeckActivityType.FLASHCARD),
                BlockOptionSpec(DeckTopic.HIRAGANA, DeckActivityType.QUIZ),
            ),
        ),
        StarterPack(
            id = "pack_katakana",
            title = "Katakana Starter",
            subtitle = "Table, Flashcards & Quiz",
            blocks = listOf(
                BlockOptionSpec(DeckTopic.KATAKANA, DeckActivityType.CHART),
                BlockOptionSpec(DeckTopic.KATAKANA, DeckActivityType.FLASHCARD),
                BlockOptionSpec(DeckTopic.KATAKANA, DeckActivityType.QUIZ),
            ),
        ),
        StarterPack(
            id = "pack_kana_review",
            title = "Daily Kana Review",
            subtitle = "Hiragana + Katakana Flashcards & SRS",
            blocks = listOf(
                BlockOptionSpec(DeckTopic.HIRAGANA, DeckActivityType.FLASHCARD),
                BlockOptionSpec(DeckTopic.KATAKANA, DeckActivityType.FLASHCARD),
                BlockOptionSpec(DeckTopic.ALL_REVIEW, DeckActivityType.REVIEW),
            ),
        ),
    )
}
