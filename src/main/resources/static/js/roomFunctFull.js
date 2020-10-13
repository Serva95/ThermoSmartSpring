function isminimum() {
    let max = parseFloat($("#tempMax").val());
    let min = parseFloat($("#tempMin").val());
    let absmin = parseFloat($("#tempMinAbs").val());
    let $inviadati = $("#inviadati");
    if (!(isNaN(min) || isNaN(absmin) || isNaN(max))) {
        if(max > 99 || max <= 10 || min > 99 || min <= 10 || absmin < 0 || absmin > 10 || max <= min) {
            //ConfermaTemp
            $("#confermaTemp").text("Errore, controlla che i dati rispettino le regole di inserimento");
            $inviadati.prop('disabled', true);
            $inviadati.val("Errore nelle temperature");
        }else{
            $("#confermaTemp").text("Confermate");
            $("#maxTemp").val(max);
            $("#minTemp").val(min);
            $("#absoluteMin").val(absmin);
            $("#tempMaxConfirm").text(max+"\xB0C");
            $("#tempMinConfirm").text(min+"\xB0C");
            $("#tempMinAbsConfirm").text(absmin+"\xB0C");
            $inviadati.prop('disabled', false);
            $inviadati.val("Conferma i dati e salva la stanza");
        }
    }else{
        $("#confermaTemp").text("Completa tutti i campi prima di premere questo bottone");
    }
}
function increase(id) {
    let obj = $("#"+id);
    let calc = 0
    if (id === "tempMinAbs"){
        if ("" !== obj.val() && parseFloat(obj.val()) < 10) {
            calc = Math.round((parseFloat(obj.val()) + 0.1) * 10) / 10
        } else { calc = 10 }
    }else {
        if (calc === 0) {calc = 10}
        if ("" !== obj.val() && parseFloat(obj.val()) < 99) {
            calc = Math.round((parseFloat(obj.val()) + 0.1) * 10) / 10
        } else { parseFloat(obj.val()) }
    }
    obj.val(calc);
}
function decrease(id) {
    let obj = $("#"+id);
    let calc;
    "" !== obj.val() ? calc = parseFloat(obj.val()) : calc = 0
    if (id === "tempMinAbs" && calc > 0) {
            calc = Math.round((parseFloat(obj.val()) - 0.1) * 10) / 10
    } else {
        if (calc > 10.1) {
            calc = Math.round((parseFloat(obj.val()) - 0.1) * 10) / 10
        }
    }
    obj.val(calc);
}