# Created by Xinyu Zhu on 8/21/2022, 8:49 PM
import struct
from node.vmdNode.models import MotionData

def read_pmx(motion_file: str):
    with open(motion_file, 'rb') as f:
        raw = f.read()

    k_frames = struct.unpack('I', raw[50:54])[0]  # Unsigned int, Number of keyframes
    k_data = raw[54:]

    k_data = k_data[111 * (k_frames - 1):]
    last_frame = struct.unpack('I', k_data[15:19])[0]
    return k_frames, last_frame


def decode_bonename(bone_015):
    return bone_015.decode("shift-JIS")


def load_data(motion_file: str):
    try:
        with open(motion_file, 'rb') as f:
            raw = f.read()
    except FileNotFoundError:
        print('%s was not found.')
        exit()

    # decode version
    version = raw[0:30]
    print('MMD version: %s' % version.decode('utf-8'))

    # decode model
    try:
        model = raw[30:50].replace(b'\x00', b'').decode('utf-8')
        print('MMD model 8: %s' % model)
    except UnicodeDecodeError:
        try:
            model = raw[30:50].replace(b'\x00', b'').decode('utf-16')
            print('MMD model 16: %s' % model)
        except UnicodeDecodeError:
            print('No model is present. This is probably camera data.\nWill try to process camera data only.')

    all_data = []
    k_frames = struct.unpack('I', raw[50:54])[0]  # Unsigned int, Number of keyframes
    k_data = raw[54:]

    for i in range(k_frames):
        offset = 111 * i
        bone = k_data[offset:offset + 15].hex()
        frame = struct.unpack('I', k_data[offset + 15:offset + 19])[0]
        # Motion relative to default position
        xc = struct.unpack('f', k_data[offset + 19: offset + 23])[0]
        yc = struct.unpack('f', k_data[offset + 23: offset + 27])[0]
        zc = struct.unpack('f', k_data[offset + 27: offset + 31])[0]
        xr = struct.unpack('f', k_data[offset + 31: offset + 35])[0]
        yr = struct.unpack('f', k_data[offset + 35: offset + 39])[0]
        zr = struct.unpack('f', k_data[offset + 39: offset + 43])[0]
        zw = struct.unpack('f', k_data[offset + 43: offset + 47])[0]



        # Interpolation data
        i_data = k_data[offset + 47: offset + 111].hex()

        try:
            # motion_keyframes.append((k_data[0:15].decode("shift-JIS"), frame, xc, yc, zc, xr, yr, zr, zw, i_data))

            bone_name = bone_to_readable(k_data[offset + 0: offset + 15].decode("shift-JIS"))
            motion_data = MotionData(bone_name, frame, xc, yc, zc, xr, yr, zr, zw)
            all_data.append(motion_data)
            # if "_" not in bone_name:
            #     print((bone_name, frame, xc, yc, zc, xr, yr, zr, zw))
        except Exception as e:
            print(e)
            k = input("enter something to continue")
    return all_data

def decode_pmx2(motion_file: str):
    try:
        with open(motion_file, 'rb') as f:
            raw = f.read()
    except FileNotFoundError:
        print('%s was not found.')
        exit()

    # decode version
    version = raw[0:30]
    print('MMD version: %s' % version.decode('utf-8'))

    # decode model
    try:
        model = raw[30:50].replace(b'\x00', b'').decode('utf-8')
        print('MMD model 8: %s' % model)
    except UnicodeDecodeError:
        try:
            model = raw[30:50].replace(b'\x00', b'').decode('utf-16')
            print('MMD model 16: %s' % model)
        except UnicodeDecodeError:
            print('No model is present. This is probably camera data.\nWill try to process camera data only.')

    k_frames = struct.unpack('I', raw[50:54])[0]  # Unsigned int, Number of keyframes
    k_data = raw[54:]

    for i in range(k_frames):
        offset = 111 * i
        bone = k_data[offset:offset + 15].hex()
        frame = struct.unpack('I', k_data[offset + 15:offset + 19])[0]
        # Motion relative to default position
        xc = struct.unpack('f', k_data[offset + 19: offset + 23])[0]
        yc = struct.unpack('f', k_data[offset + 23: offset + 27])[0]
        zc = struct.unpack('f', k_data[offset + 27: offset + 31])[0]
        xr = struct.unpack('f', k_data[offset + 31: offset + 35])[0]
        yr = struct.unpack('f', k_data[offset + 35: offset + 39])[0]
        zr = struct.unpack('f', k_data[offset + 39: offset + 43])[0]
        zw = struct.unpack('f', k_data[offset + 43: offset + 47])[0]

        # Interpolation data
        i_data = k_data[offset + 47 : offset + 111].hex()

        try:
            # motion_keyframes.append((k_data[0:15].decode("shift-JIS"), frame, xc, yc, zc, xr, yr, zr, zw, i_data))

            bone_name = bone_to_readable(k_data[offset + 0 : offset + 15].decode("shift-JIS"))
            if "_" not in bone_name:
                print((bone_name, frame, xc, yc, zc, xr, yr, zr, zw))
        except Exception as e:
            print(e)
            pass


def bone_to_readable(bone_name:str):
    return bone_name.replace("\x00", "")

def decode_pmx(motion_file: str):
    # Load VMD file
    raw = None
    try:
        with open(motion_file, 'rb') as f:
            raw = f.read()
    except FileNotFoundError:
        print('%s was not found.')
        exit()

    version = raw[0:30]

    print('MMD version: %s' % version.decode('utf-8'))

    try:
        model = raw[30:50].replace(b'\x00', b'').decode('utf-8')
        print('MMD model 8: %s' % model)
        model = raw[30:50].hex()

    except UnicodeDecodeError:
        try:
            model = raw[30:50].replace(b'\x00', b'').decode('utf-16')

            print('MMD model 16: %s' % model)
            model = raw[30:50].hex()
        except UnicodeDecodeError:

            print('No model is present. This is probably camera data.\nWill try to process camera data only.')
            motion = 0
            face = 0
            camera = 2
            model = raw[30:50].hex()

    k_frames = struct.unpack('I', raw[50:54])[0]  # Unsigned int, Number of keyframes
    k_data = raw[54:]

    # Process motion data.
    motion_keyframes = []

    print('Motion frames: %i' % k_frames)

    print('Processing motion data. This may take a few seconds...')
    for i in range(k_frames):
        # print(k_data[0:73])
        # Bone name, I don't know how to decode this shit
        bone = k_data[0:15].hex()
        # try:
        #     print(i, k_data[0:15].decode("shift-JIS"))
        # except:
        #     pass
        # Frame number
        frame = struct.unpack('I', k_data[15:19])[0]

        # Motion relative to default position
        xc = struct.unpack('f', k_data[19:23])[0]
        yc = struct.unpack('f', k_data[23:27])[0]
        zc = struct.unpack('f', k_data[27:31])[0]
        xr = struct.unpack('f', k_data[31:35])[0]
        yr = struct.unpack('f', k_data[35:39])[0]
        zr = struct.unpack('f', k_data[39:43])[0]
        zw = struct.unpack('f', k_data[43:47])[0]

        # Interpolation data
        i_data = k_data[47:111].hex()
        # Add keyframe to list of keyframes
        try:
            motion_keyframes.append((k_data[0:15].decode("shift-JIS"), frame, xc, yc, zc, xr, yr, zr, zw, i_data))
            print((k_data[0:15].decode("shift-JIS"), frame, xc, yc, zc, xr, yr, zr, zw))
        except Exception as e:
            print(e)
            pass
        # jump to next frame
        k_data = k_data[111:]

    # pprint(motion_keyframes)


def dump_json(all_data, output="output.json"):
    a = list()
    with open(output, mode="w", encoding="utf-8") as f:
        for data in all_data:
            a.append(data)
            f.write(data.to_json() + "\n")
        # f.write(json.dumps(a))


if __name__ == '__main__':
    all_data = load_data("C:/Software/MMD/MikuMikuDanceE_v932x64/UserFile/Motion/external/mobiusP/CH4NGE_motion/CH4NGE.vmd")
    print(len(all_data))
    dump_json(all_data)
    # decode_pmx(
    #     "C:/myC/Personal/3DWorkSpace/Project/youla/motion.vmd")
