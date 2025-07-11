package com.example.equipocuatro.view.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.equipocuatro.R
import com.example.equipocuatro.retrofit.Breed
import com.example.equipocuatro.retrofit.RetrofitClient
import com.example.equipocuatro.databinding.FragmentEditAppointmentBinding
import com.example.equipocuatro.model.Appointment
import com.example.equipocuatro.retrofit.ImagenResponse
import com.example.equipocuatro.viewmodel.AppointmentViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentEditAppointment.newInstance] factory method to
 * create an instance of this fragment.
 */

class FragmentEditAppointment : Fragment() {
    private lateinit var binding: FragmentEditAppointmentBinding
    private val app: AppointmentViewModel by viewModels()
    private lateinit var receivedAppointment: Appointment

    val ERROR_MESSAGE = "Ocurrió un error!"

    lateinit var field_name : EditText
    lateinit var field_breed : EditText
    lateinit var field_owner : EditText
    lateinit var field_tel : EditText
    lateinit var btnEdit : Button

    lateinit var photo : String
    lateinit var progressBar : ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditAppointmentBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // Asignación de variables
        field_name = binding.editTextName
        field_breed = binding.editTextBreed
        field_owner = binding.editTextOwner
        field_tel = binding.editTextTelephone
        btnEdit = binding.editButton
        progressBar = binding.progressBar

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Funciones necesarias
        controladores()
        loadAppointment()
        getBreeds()
        setToolbar()
        validateData()
    }

    private fun controladores() {
        validateData()
        btnEdit.setOnClickListener {
            try {
                if (receivedAppointment.breed != field_breed.text.toString()) {
                    getImage(field_breed.text.toString()) { imageUrl ->
                        updateAppointment(imageUrl)
                    }
                } else {
                    updateAppointment(receivedAppointment.photo) // Usa la foto existente si la raza no cambia
                }
            } catch(e: Exception) {
                Log.d("error", e.toString())
                Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private fun controladores() {
//        validateData()
//        btnEdit.setOnClickListener {
//            try {
//                getImage(field_breed.text.toString())
//            } catch(e: Exception) {
//                Log.d("error",e.toString())
//                Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
//
//            }
//        }
//    }

    private fun getBreeds (){
        val retrofitBring = RetrofitClient.consumeApi.getBring()
        retrofitBring.enqueue(object : Callback<Breed>{
            override fun onResponse(call: Call<Breed>, response: Response<Breed>) {
                val breedList = response.body()?.message ?: emptyList()
                setupAutoCompleteTextView(breedList)
            }

            override fun onFailure(call: Call<Breed>, t: Throwable) {
                Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getImage(breed: String, callback: (String?) -> Unit) {
        try {
            progressBar.visibility = View.VISIBLE
            val retrofitBring = RetrofitClient.consumeApi.getRandomDogImage(breed)
            retrofitBring.enqueue(object : Callback<ImagenResponse> {
                override fun onResponse(call: Call<ImagenResponse>, response: Response<ImagenResponse>) {
                    progressBar.visibility = View.GONE
                    val imageBreed = response.body()?.message
                    callback(imageBreed)
                }

                override fun onFailure(call: Call<ImagenResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            })
        } catch (e: Exception) {
            progressBar.visibility = View.GONE
            Log.d("error: ", e.toString())
            Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
            callback(null)
        }
    }


//    private fun getImage (breed : String) {
//        try {
//            progressBar.visibility = View.VISIBLE
//            val retrofitBring = RetrofitClient.consumeApi.getRandomDogImage(breed)
//            retrofitBring.enqueue(object : Callback<ImagenResponse> {
//                override fun onResponse(
//                    call: Call<ImagenResponse>,
//                    response: Response<ImagenResponse>
//                ) {
//                    val image_breed = response.body()?.message ?: ""
//                    photo = image_breed
//                    updateAppointment()
//                    progressBar.visibility = View.INVISIBLE
//                }
//
//                override fun onFailure(call: Call<ImagenResponse>, t: Throwable) {
//                    Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
//                    progressBar.visibility = View.VISIBLE
//                }
//            })
//        } catch (e:Exception){
//            Log.d("error: ", e.toString())
//            Toast.makeText(requireContext(), ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun setupAutoCompleteTextView(breedList: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, breedList)
        (binding.editTextBreed as? AutoCompleteTextView)?.apply {
            setAdapter(adapter)
            threshold = 2 // Configura el número de caracteres que se deben escribir antes de que se muestren las sugerencias
        }
    }

    private fun setToolbar (){
        val toolbar : Toolbar = binding.contentToolbar.toolbarEdit
        toolbar.setNavigationOnClickListener {
            // Navegar a otro fragmento cuando se hace clic en el icono de navegación
            findNavController().popBackStack()
        }
    }

    private fun updateButtonState() {
        if (btnEdit.isEnabled) {
            btnEdit.setTextColor(Color.WHITE)
            btnEdit.setTypeface(null, Typeface.BOLD)
            btnEdit.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.strong_pink))
        } else {
            btnEdit.setTextColor(ContextCompat.getColor(requireContext(), R.color.lightgrey))
            btnEdit.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.strong_pink))
        }
    }

    private fun validateData() {
        val listEditText = listOf(field_name, field_breed, field_owner, field_tel)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No necesitamos hacer nada aquí
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No necesitamos hacer nada aquí
            }

            override fun afterTextChanged(s: Editable?) {
                val isListFull = listEditText.all { it.text.isNotEmpty() }
                btnEdit.isEnabled = isListFull
                updateButtonState()
            }
        }

        for (editText in listEditText) {
            editText.addTextChangedListener(textWatcher)
        }
    }

    private fun loadAppointment() {
        val receivedBundle = arguments
        receivedAppointment = receivedBundle?.getSerializable("dataAppointment") as Appointment
        field_name.setText(receivedAppointment.name_pet)
        field_breed.setText(receivedAppointment.breed)
        field_owner.setText(receivedAppointment.name_owner)
        field_tel.setText(receivedAppointment.phone_number)
    }

    private fun updateAppointment(imageUrl: String?) {
        try {
            // Get updated appointment data from EditTexts
            val idApp = receivedAppointment.id
            val photoAppointment = imageUrl ?: receivedAppointment.photo
            val updatedName = field_name.text.toString()
            val updatedBreed = field_breed.text.toString()
            val updatedOwner = field_owner.text.toString()
            val updatedTelephone = field_tel.text.toString()
            val symptoms = receivedAppointment.symptoms

            val appointment = Appointment(id = idApp, photo = photoAppointment,
                name_pet = updatedName, breed = updatedBreed,
                name_owner = updatedOwner, phone_number = updatedTelephone,
                symptoms = symptoms)

            app.updateAppointment(appointment)

            findNavController().navigate(R.id.action_editAppointmentFragment_to_homeFragment)
        } catch(e: Exception){
            Log.d("error: ", e.toString())
        }
    }
}