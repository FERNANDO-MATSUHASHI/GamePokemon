package com.example.gamepokemon.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.apppokemon.PokemonsViewModel
import com.example.apppokemon.imagemUrl
import com.example.apppokemon.infoAltura
import com.example.apppokemon.infoPeso
import com.example.gamepokemon.R
import com.example.gamepokemon.dados.NUMERO_DE_PALAVRAS
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


@Preview(showBackground = true)
@Composable
fun GamePokemon() {
    TelaDeJogo(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun TelaDeJogo(
        modifier: Modifier = Modifier,
        jogoViewModel: JogoViewModel = viewModel()
) {
    // Usando a instância do jogoViewModel receba por este componente,
    // declaramos uma variável local para acessar as variáveis de estado
    // em modo SOMENTE LEITURA
    val estadoDoJogo by jogoViewModel.estadoAtual.collectAsState()

    val offset = Offset(3.0f, 4.0f)

    var expanded by remember { mutableStateOf (false) }

    val pokemonsViewModel: PokemonsViewModel = viewModel()

    // Título do jogo
    Text(
        text = "Game Pokemon",
        fontSize = 35.sp,
        style = TextStyle(
            fontSize = 24.sp,
            shadow = Shadow(
                color = Color.Blue, blurRadius = 3f, offset = offset
            )
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Componente do jogo principal
        JogoPrincipal(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(12.dp),
            contadorDePalavras = estadoDoJogo.contadorPalavras,
            pokemonAtual = pokemonAtual
        )
        if (estadoDoJogo.informacao) {
            // Botão para Próxima
            OutlinedButton(
                onClick = { jogoViewModel.proximo() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.next),
                    fontSize = 16.sp
                )
            }
        } else {
            OutlinedButton(
                onClick = { jogoViewModel.pularJogada() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.skip),
                    fontSize = 16.sp
                )
            }
        }
        // Botão para informações do Pokemon
        Button(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = estadoDoJogo.informacao
        ) {
            Text(
                text = stringResource(id = R.string.information),
                fontSize = 16.sp
            )
        }
        PlacarDeJogo(placar = estadoDoJogo.placar)

        if (estadoDoJogo.gameOver) {
            DialogoFimDeJogo(
                placar = estadoDoJogo.placar,
                onJogarDeNovo = { jogoViewModel.reiniciarJogo() },
            )
        }

        if (expanded) {
            pokemonsViewModel.obterPokemonPesoAltura(pokemonAtual)
            AlertDialog(
                onDismissRequest = {

                },
                confirmButton = {
                    Button(
                        onClick = { expanded = false }
                    ) {
                        Text(text = "Ok")
                    }
                },
                title = {
                    Row {
                        Text(
                            "Informações",
                            textDecoration = TextDecoration.Underline,
                            color = Color.Black
                            )
                    }
                },
                text = {
                    Column {
                        Row(
                            modifier = Modifier.padding(bottom = 20.dp, top = 30.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Nome: ",
                                fontSize = 18.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = pokemonAtual,
                                fontSize = 18.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                        Row {
                            Text(
                                text = "Peso: ",
                                fontSize = 18.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = infoPeso,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Row {
                            Text(
                                text = "Altura: ",
                                fontSize = 18.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = infoAltura,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(15.dp)
            )
        }
    }
}

@Composable
fun JogoPrincipal(
    modifier: Modifier = Modifier,
    contadorDePalavras: Int,
    pokemonAtual: String,
    jogoViewModel: JogoViewModel = viewModel()
) {

    val estadoDoJogo by jogoViewModel.estadoAtual.collectAsState()

    val pokemonsViewModel: PokemonsViewModel = viewModel()
    pokemonsViewModel.obterPokemonImagem(pokemonAtual)
    val _estadoAtual = MutableStateFlow(EstadoDoJogo())

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            // centralizar horizontal
            horizontalAlignment = Alignment.CenterHorizontally,
            // define um padding padrão vertical entre os widgets dentro da coluna
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = modifier
                //.padding(16.dp)
                .fillMaxWidth()
        ) {
            // Contagem de palavras do jogo
            Text(
                text = stringResource(id = R.string.word_count, contadorDePalavras, NUMERO_DE_PALAVRAS),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(alignment = Alignment.End),
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = stringResource(id = if (!estadoDoJogo.errou && !estadoDoJogo.acertou) R.string.which_type_of_pokemon_is_this else if (estadoDoJogo.errou) R.string.wrong_guess else R.string.got_it_right ),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    lineHeight = 35.sp,
                )
            )
            // AsyncImage para exibir a imagem do Pokemon
            AsyncImage(
                model = ImageRequest.Builder(
                    context = LocalContext.current
                )
                    .data(imagemUrl)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                colorFilter = if (estadoDoJogo.informacao) null else ColorFilter.tint(Color.Black)
            )

            // Declare as variáveis de estado necessárias
            var selectedOption by remember { mutableStateOf("") }
            var correctOption by remember { mutableStateOf("") }


            // Dentro do composable onde você está construindo a lista de botões
            listOption.forEach  { option ->
                Button(
                    onClick = {

                        selectedOption = option
                        correctOption = pokemonAtual
                        jogoViewModel.verificarAcerto(option)

                        if (!estadoDoJogo.errou) {
                            _estadoAtual.update { currentState ->
                                currentState.copy(
                                    selectedOptionBackgroundColor = Color.Red,
                                    correctOptionBackgroundColor = Color.Green,
                                )
                            }
                        }
                    },
                    //enabled = buttonSel[index],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedOption == option) _estadoAtual.value.selectedOptionBackgroundColor else if (option == correctOption) _estadoAtual.value.correctOptionBackgroundColor else _estadoAtual.value.originalOptionBackgroundColor,
                        contentColor = if (selectedOption == option) Color.White else Color.Black
                    )
                ) {
                    Text(text = option)
                }
            }

        }
    }
}

@Composable
fun PlacarDeJogo(placar: Int) {
    Card(
        modifier = Modifier.padding(top = 10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.score, placar),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun DialogoFimDeJogo(
    placar: Int,
    onJogarDeNovo: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Esta variável dá acesso à tela principal (MainActivity) para podermos fechar o jogo
    // quando o jogador clicar no botão indicado
    val activity = (LocalContext.current as Activity)
    AlertDialog(
        // Deixando esse evento vazio, o jogador pode fechar o diálogo simplesmente tocando
        // em algum lugar fora dele
        onDismissRequest = { /*TODO*/ },
        confirmButton = {
            TextButton(
                onClick = onJogarDeNovo
            ) {
                Text(text = "Jogar novamente")
            } },
        dismissButton = {
            TextButton(
                onClick = { activity.finish() }
            ) {
                Text(text = stringResource(id = R.string.exit))
            } },
        title = { Text(
            text = if (placar == 0) {
                stringResource(id = R.string.it_wasn_t_this_time)
            } else {
                stringResource(id = R.string.congratulation)
            }
        ) },
        text = {
            Text(text = stringResource(id = R.string.you_scored, placar)
            )},
        modifier = modifier,
    )
}