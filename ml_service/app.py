from flask import Flask, request, jsonify
import pickle
import numpy as np

app = Flask(__name__)

# ================= LOAD MODEL =================
with open("model_cart.pkl", "rb") as f:
    model_cart = pickle.load(f)

with open("model_rf.pkl", "rb") as f:
    model_rf = pickle.load(f)

# ================= ENDPOINT ===================
@app.route("/predict-panen", methods=["POST"])
def predict_panen():
    data = request.json

    try:
        X = np.array([[
            float(data["luas_lahan"]),
            float(data["curah_hujan"]),
            float(data["kualitas_tanah"]),
            float(data["jumlah_pupuk"]),
            float(data["lama_tanam"])
        ]])

        cart_pred = model_cart.predict(X)[0]
        rf_pred   = model_rf.predict(X)[0]

        return jsonify({
            "cart": round(float(cart_pred), 2),
            "random_forest": round(float(rf_pred), 2)
        })

    except Exception as e:
        return jsonify({
            "error": str(e)
        }), 400


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=True)
