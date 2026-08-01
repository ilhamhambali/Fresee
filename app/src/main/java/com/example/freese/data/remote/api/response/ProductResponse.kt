import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Model untuk data Penjual (Owner) di dalam Produk
@Parcelize
data class ProductOwner(
	@SerializedName("full_name") val fullName: String,
	@SerializedName("farm_name") val farmName: String?
): Parcelable

// Model untuk List Produk (Get All & Get My Products)
@Parcelize
data class ProductResponse(
	@SerializedName("id") val id: Int,
	@SerializedName("name") val name: String,
	@SerializedName("price") val price: Int,
	@SerializedName("stock") val stock: Int,
	@SerializedName("unit") val unit: String?,
	@SerializedName("category") val category: String,
	@SerializedName("description") val description: String,
	@SerializedName("image") val image: String?,
	@SerializedName("owner") val owner: ProductOwner?
) : Parcelable


// Model untuk Response setelah berhasil Tambah Produk
data class AddProductResponse(
	@SerializedName("id") val id: Int,
	@SerializedName("name") val name: String,
	@SerializedName("price") val price: Int,
	@SerializedName("stock") val stock: Int,
	@SerializedName("image") val image: String,
	@SerializedName("ownerId") val ownerId: Int
)