package com.cericatto.scribbledash.ui.statistics

sealed interface StatisticsScreenAction {
	data object NavigateUp : StatisticsScreenAction
	data object OnTryAgainClicked : StatisticsScreenAction
}