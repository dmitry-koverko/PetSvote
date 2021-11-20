package com.petsvote.app

import android.R.attr
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petsvote.api.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.R.attr.password
import com.petsvote.api.entity.Location
import com.petsvote.soket.SocketClient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class BlankViewModel : ViewModel() {

    fun get(){
        viewModelScope.launch (Dispatchers.IO){

            //Log.d("ATG", NetworkService().createService().getBreeds("cat", "ru").toString())
            //Log.d("ATG", NetworkService().createService().getLocalization( "ru").toString())
            //Log.d("ATG", NetworkService().createService().getGlobalConfig( 10800).toString())
            /*Log.d("ATG", NetworkService().createService().getPets(
                50, 0, "uk","dog", null, null, null, "0:30", "global", null
            ).toString())*/
            /*Log.d("ATG", NetworkService().createService().getRating(
                null, null, "ru","cat", null, null, null,
                "0:30", "global", 3829, null
            ).toString())*/

            /*Log.d("ATG", NetworkService().createService().addVote(
                1772448, 1480675, 5, true,
                "kot", "", 1).toString())*/

            /*Log.d("ATG", NetworkService().createService().getPetDetails(
                "cat", "MALE", 282, 3, "0:3",
                1488736, 1588589, null).toString())*/

            //Log.d("ATG", NetworkService().createService().getCurrentUser("ru").toString())

            /*
            Map<String, RequestBody> map = new HashMap<>();
            map.put("Id", AZUtils.toRequestBody(eventId));
            map.put("Name", AZUtils.toRequestBody(titleView.getValue()));

            if (imageUri != null) {
                  File file = new File(imageUri.getPath());
                  RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
                  map.put("file\"; filename=\"pp.png\"", fileBody);
            }
             */


            /*val last_name = "Norton"
            val ln_rb = last_name.toRequestBody("text/plain".toMediaTypeOrNull())
            val map = mutableMapOf<String, RequestBody>()
            map.put("last_name", ln_rb)
            val location = Location(282, 3, "Belarus", "Минск")
            val lc_sonn = Json.encodeToString(location)
            Log.d("TAG", NetworkService().createService().saveUserData(
                map, "adada", "dadadad", lc_sonn).toString())*/


            SocketClient.newInstance().getDatails()

        }
    }

}