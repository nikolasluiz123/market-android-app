package br.com.market.servicedataaccess.webclients

import android.content.Context
import android.util.Log
import br.com.market.core.R
import br.com.market.core.preferences.dataStore
import br.com.market.core.preferences.getToken
import br.com.market.core.utils.TokenUtils
import br.com.market.servicedataaccess.responses.types.AuthenticationResponse
import br.com.market.servicedataaccess.responses.types.MarketServiceResponse
import br.com.market.servicedataaccess.responses.types.PersistenceResponse
import br.com.market.servicedataaccess.responses.types.ReadResponse
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException

/**
 * Classe base para ser implementada pelos WebClients.
 *
 * Ela possui implementações que são comuns entre os WebClients e
 * evita a duplicidade de código, além de centralizar a implementação.
 *
 * @author Nikolas Luiz Schmitt
 *
 * @property context Contexto de uso diversificado
 */
open class BaseWebClient(private val context: Context) {

    /**
     * Função utilizada para recuperar o token de autenticação do DataStore
     * para ser possível realizar a autenticação no serviço e permitir a chamada
     * dos end points.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun getToken() = TokenUtils.formatToken(context.dataStore.getToken()!!)

    /**
     * Função que pode ser utilizada para envolver o bloco de código em um tratamento padrão
     * de exceções que podem ocorrer ao tentar estabelecer uma conexão com o serviço.
     *
     * A função permite que sejam adicionados novos tratamentos específicos de uma rotina,
     * esses tratamentos específicos devem seguir um padrão:
     *
     * Para realizar o tratamento, utilize o when da mesma forma como é feito nessa implementação,
     * além disso, seus tratamentos deverão sempre terminar tratando uma Exception, pois, da forma
     * que é feito atualmente está localizada no else do when, e é onde as Exceptions cairiam pro
     * padrão.
     *
     * Essa função é preparada para o cenário de Persistência, note que ela obriga o retorno de
     * uma PersistenceResponse, portanto, utilize-a nesses cenários.
     *
     * @param codeBlock Bloco de código que deseja executar e tratar.
     * @param customExceptions Caso haja necessidade de tratar exceções em um caso específico, use este atributo.
     *
     * @exception SocketTimeoutException Pode ocorrer essa exceção quando o serviço demorar para responder,
     * nesse caso podemos considerar como sucesso pois nós salvamos as alterações do produto localmente e permitimos uma
     * sincronização dos dados.
     *
     * @exception ConnectException Pode ocorrer essa exceção quando o usuário não possuir conexão de internet
     * no dispositivo, nesse caso podemos considerar como sucesso pois nós salvamos as alterações do produto localmente e permitimos
     * uma sincronização dos dados.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun persistenceServiceErrorHandlingBlock(
        codeBlock: suspend () -> PersistenceResponse,
        customExceptions: (e: Exception) -> PersistenceResponse = {
            Log.e("Error", "persistenceServiceErrorHandlingBlock: ", it)

            PersistenceResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                error = context.getString(R.string.unknown_error_message, it.message)
            )
        }
    ): PersistenceResponse {
        return try {
            codeBlock()
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> {
                    PersistenceResponse(
                        code = HttpURLConnection.HTTP_UNAVAILABLE,
                        success = false,
                        error = context.getString(R.string.connection_server_error_message)
                    )
                }
                is ConnectException -> {
                    PersistenceResponse(
                        code = HttpURLConnection.HTTP_UNAVAILABLE,
                        success = false,
                        error = context.getString(R.string.connection_server_error_message)
                    )
                }
                else -> customExceptions(e)
            }
        }
    }

    /**
     * Função que pode ser utilizada para envolver o bloco de código em um tratamento padrão
     * de exceções que podem ocorrer ao tentar estabelecer uma conexão com o serviço.
     *
     * A função permite que sejam adicionados novos tratamentos específicos de uma rotina,
     * esses tratamentos específicos devem seguir um padrão:
     *
     * Para realizar o tratamento, utilize o when da mesma forma como é feito nessa implementação,
     * além disso, seus tratamentos deverão sempre terminar tratando uma Exception, pois, da forma
     * que é feito atualmente está localizada no else do when, e é onde as Exceptions cairiam pro
     * padrão.
     *
     * Essa função é preparada para o cenário de Autenticação, note que ela obriga o retorno de
     * uma AuthenticationResponse, portanto, utilize-a nesses cenários.
     *
     * @param codeBlock Bloco de código que deseja executar e tratar.
     * @param customExceptions Caso haja necessidade de tratar exceções em um caso específico, use este atributo.
     *
     * @exception SocketTimeoutException Pode ocorrer essa exceção quando o serviço demorar para responder,
     * nesse caso podemos considerar como sucesso pois nós salvamos as alterações do produto localmente e permitimos uma
     * sincronização dos dados.
     *
     * @exception ConnectException Pode ocorrer essa exceção quando o usuário não possuir conexão de internet
     * no dispositivo, nesse caso podemos considerar como sucesso pois nós salvamos as alterações do produto localmente e permitimos
     * uma sincronização dos dados.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun authenticationServiceErrorHandlingBlock(
        codeBlock: suspend () -> AuthenticationResponse,
        customExceptions: (e: Exception) -> AuthenticationResponse = {
            Log.e("Error", "authenticationServiceErrorHandlingBlock: ", it)

            AuthenticationResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                error = context.getString(R.string.unknown_error_message, it.message)
            )
        }
    ): AuthenticationResponse {
        return try {
            codeBlock()
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException, is ConnectException -> {
                    AuthenticationResponse(
                        code = HttpURLConnection.HTTP_UNAVAILABLE,
                        error = context.getString(R.string.connection_server_error_message)
                    )
                }
                else -> customExceptions(e)
            }
        }
    }

    /**
     * Função que pode ser utilizada para envolver o bloco de código em um tratamento padrão
     * de exceções que podem ocorrer ao tentar estabelecer uma conexão com o serviço.
     *
     * A função permite que sejam adicionados novos tratamentos específicos de uma rotina,
     * esses tratamentos específicos devem seguir um padrão:
     *
     * Para realizar o tratamento, utilize o when da mesma forma como é feito nessa implementação,
     * além disso, seus tratamentos deverão sempre terminar tratando uma Exception, pois, da forma
     * que é feito atualmente está localizada no else do when, e é onde as Exceptions cairiam pro
     * padrão.
     *
     * Essa função é preparada para o cenário de leitura de dados do serviço, note que ela obriga o retorno de
     * uma ReadResponse, portanto, utilize-a nesses cenários.
     *
     * @param codeBlock Bloco de código que deseja executar e tratar.
     * @param customExceptions Caso haja necessidade de tratar exceções em um caso específico, use este atributo.
     *
     * @exception SocketTimeoutException Pode ocorrer essa exceção quando o serviço demorar para responder,
     * nesse caso podemos considerar como sucesso pois nós salvamos as alterações do produto localmente e permitimos uma
     * sincronização dos dados.
     *
     * @exception ConnectException Pode ocorrer essa exceção quando o usuário não possuir conexão de internet
     * no dispositivo, nesse caso podemos considerar como sucesso pois nós salvamos as alterações do produto localmente e permitimos
     * uma sincronização dos dados.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun <SDO> readServiceErrorHandlingBlock(
        codeBlock: suspend () -> ReadResponse<SDO>,
        customExceptions: (e: Exception) -> ReadResponse<SDO> = {
            Log.e("Error", "readServiceErrorHandlingBlock: ", it)

            ReadResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                success = false,
                error = context.getString(R.string.unknown_error_message, it.message),
            )
        }
    ): ReadResponse<SDO> {
        return try {
            codeBlock()
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> {
                    ReadResponse(
                        code = HttpURLConnection.HTTP_UNAVAILABLE,
                        success = false,
                        error = context.getString(R.string.connection_server_error_message)
                    )
                }
                is ConnectException -> {
                    ReadResponse(
                        code = HttpURLConnection.HTTP_UNAVAILABLE,
                        success = false,
                        error = context.getString(R.string.connection_server_error_message)
                    )
                }
                else -> customExceptions(e)
            }
        }
    }

    /**
     * Função que pode ser utilizada para envolver o bloco de código em um tratamento padrão
     * de exceções que podem ocorrer ao tentar estabelecer uma conexão com o serviço.
     *
     * A função permite que sejam adicionados novos tratamentos específicos de uma rotina,
     * esses tratamentos específicos devem seguir um padrão:
     *
     * Para realizar o tratamento, utilize o when da mesma forma como é feito nessa implementação,
     * além disso, seus tratamentos deverão sempre terminar tratando uma Exception, pois, da forma
     * que é feito atualmente está localizada no else do when, e é onde as Exceptions cairiam por
     * padrão.
     *
     * Essa função é preparada para todos os cenários que não são de Persistência, note que ela
     * obriga o retorno de uma MarketServiceResponse, portanto, utilize-a nesses cenários.
     *
     * @param codeBlock Bloco de código que deseja executar e tratar.
     * @param customExceptions Caso haja necessidade de tratar exceções em um caso específico, use este atributo.
     *
     * @exception SocketTimeoutException Pode ocorrer essa exceção quando o serviço demorar para responder,
     * nesse caso podemos considerar como sucesso pois nós salvamos as alterações do produto localmente e permitimos uma
     * sincronização dos dados.
     *
     * @exception ConnectException Pode ocorrer essa exceção quando o usuário não possuir conexão de internet
     * no dispositivo, nesse caso podemos considerar como sucesso pois nós salvamos as alterações do produto localmente e permitimos
     * uma sincronização dos dados.
     *
     * @author Nikolas Luiz Schmitt
     */
    suspend fun serviceErrorHandlingBlock(
        codeBlock: suspend () -> MarketServiceResponse,
        customExceptions: (e: Exception) -> MarketServiceResponse = {
            Log.e("Error", "serviceErrorHandlingBlock: ", it)

            MarketServiceResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                success = false,
                error = context.getString(R.string.unknown_error_message, it.message),
            )
        }
    ): MarketServiceResponse {
        return try {
            codeBlock()
        } catch (e: Exception) {
            when (e) {
                is SocketTimeoutException -> {
                    MarketServiceResponse(
                        code = HttpURLConnection.HTTP_UNAVAILABLE,
                        success = false,
                        error = context.getString(R.string.connection_server_error_message)
                    )
                }
                is ConnectException -> {
                    MarketServiceResponse(
                        code = HttpURLConnection.HTTP_UNAVAILABLE,
                        success = false,
                        error = context.getString(R.string.connection_server_error_message)
                    )
                }
                else -> customExceptions(e)
            }
        }
    }
}
