SuperPoll : UGen {
    *ar { arg trig, value, label, trigid = -1;
        this.multiNew('audio', trig, value, label, trigid);
        ^value;
    }
    *kr { arg trig, value, label, trigid = -1;
        this.multiNew('control', trig, value, label, trigid);
        ^value;
    }
    *new1 { arg rate, trig, value, label, trigid;
        var valueArr = value.asPair.asArray;
        label = label ?? { "%".format(value.class) };
        label = label.asString.collectAs(_.ascii, Array);
        if (trig.isNumber) { trig = Impulse.multiNew(rate, trig, 0) };
        ^super.new.rate_(rate).addToSynth.init([trig] ++ valueArr ++ [trigid, label.size] ++ label);
    }
    init { arg theInputs;
        inputs = theInputs;
        //^this.initOutputs(2, rate);
    }
}

SuperPair {
    var msd, lsd;

    *new { arg msd, lsd;
        ^super.newCopyArgs(msd, lsd);
    }

    *fromDouble { arg double = 0.0;
        var msd = Float.from32Bits(double.as32Bits);
        var lsd = Float.from32Bits((double - msd).as32Bits);
        ^super.newCopyArgs(msd, lsd);
    }

    asPair {
        ^this;
    }

    asArray {
        ^[msd, lsd];
    }

    asBig {
        ^this.asArray;
    }

    poll { arg trig = 10, label, trigid = -1;
        ^SuperPoll.ar(trig, this, label, trigid);
    }
}

+ SimpleNumber {
    asPair {
        var double = this.asFloat;
        ^SuperPair.fromDouble(double);
    }

    asBig {
        ^this.asPair.asArray;
    }
}

+ UGen {
    asPair {
        ^SuperPair(this, 0.0)
    }
}

+ Symbol {
    krBig { arg value = 0.0;
        ^SuperPair(*this.kr(value.asPair))
    }
}
